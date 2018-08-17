package com.tmlab.amalgamj;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import org.joou.UByte;
import org.joou.UInteger;
import org.joou.ULong;
import org.joou.UShort;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Serializer {

    private static final String TAG = Serializer.class.getSimpleName();

    public static String toJson(Object object) {
        GsonBuilder builder = getBuilder();
        return builder.create().toJson(object);
    }

    public static <T> T fromJson(Response response, String json, Class<T> classOfT) {
        try {
            GsonBuilder builder = getBuilder();
            return builder.create().fromJson(json, classOfT);
        } catch (Exception e) {
            Log.e(TAG, e.toString(), e);
            response.setError(e);
            return null;
        }
    }

    public static <T> T fromJson(Response response, String json, Type typeOfT, Type... typeArguments) {
        try {
            GsonBuilder builder = getBuilder();
            return builder.create().fromJson(json, TypeToken.getParameterized(typeOfT, typeArguments).getType());
        } catch (Exception e) {
            Log.e(TAG, e.toString(), e);
            response.setError(e);
            return null;
        }
    }

    public static GsonBuilder getBuilder() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(UByte.class, new UByteTypeAdapter());
        builder.registerTypeAdapter(UShort.class, new UShortTypeAdapter());
        builder.registerTypeAdapter(ULong.class, new ULongTypeAdapter());
        builder.registerTypeAdapter(UInteger.class, new UIntegerTypeAdapter());
        builder.registerTypeAdapter(PublicKey.class, new PublicKeyTypeAdapter());
        builder.registerTypeAdapter(TimePointSec.class, new TimePointSecTypeAdapter());
        builder.registerTypeAdapter(Asset.class, new AssetTypeAdapter());
        builder.registerTypeAdapter(Ripemd160.class, new Ripemd160TypeAdapter());
        builder.registerTypeAdapter(Signature.class, new SignatureTypeAdapter());
        builder.registerTypeAdapter(Optional.class, new OptionalTypeAdapter());
        builder.registerTypeAdapter(ArrayHashMap.class, new ArrayHashMapTypeAdapter());
        builder.registerTypeAdapter(Operation.class, new OperationTypeAdapter());
        return builder;
    }

    public static void serialize(ByteBuffer buffer, Object object) {
        if (object instanceof UShort) {
            buffer.writeUint16((UShort) object);
        } else if (object instanceof UInteger) {
            buffer.writeUint32((UInteger) object);
        } else if (object instanceof ULong) {
            buffer.writeUint64((ULong) object);
        } else if (object instanceof Short) {
            buffer.writeInt16((Short) object);
        } else if (object instanceof Long) {
            buffer.writeInt64((Long) object);
        } else if (object instanceof String) {
            buffer.writeVString((String) object);
        } else if (object instanceof Boolean) {
            buffer.writeUint8(UByte.valueOf((Boolean) object ? 1 : 0));
        } else if (object instanceof Operation) {
            Operation operation = (Operation) object;
            buffer.writeVarint32(operation.getId());
            for (String key : operation.params.keySet()) {
                serialize(buffer, operation.params.get(key));
            }
        } else if (object instanceof List) {
            List<Object> list = (List) object;
            buffer.writeVarint32(list.size());
            for (Object value : list) {
                serialize(buffer, value);
            }
        } else if (object instanceof Map) {
            Map<Object, Object> map = (Map) object;
            buffer.writeVarint32(map.size());
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                serialize(buffer, entry.getKey());
                serialize(buffer, entry.getValue());
            }
        } else if (object instanceof Set) {
            Set<Object> set = (Set) object;
            buffer.writeVarint32(set.size());
            for (Object value : set) {
                serialize(buffer, value);
            }
        } else if (object instanceof PublicKey) {
            buffer.write(((PublicKey) object).getEncoded());
        } else if (object instanceof TimePointSec) {
            buffer.writeInt32(((TimePointSec) object).value);
        } else if (object instanceof Optional) {
            Optional optional = (Optional) object;
            if (optional.value == null) {
                buffer.writeUint8(UByte.valueOf(0));
            } else {
                buffer.writeUint8(UByte.valueOf(1));
                serialize(buffer, optional.value);
            }
        } else if (object instanceof Asset) {
            Asset asset = (Asset) object;
            String symbol = asset.symbol.name;
            buffer.writeInt64(asset.amount);
            buffer.writeUint8(UByte.valueOf(asset.symbol.precision));
            buffer.writeString(symbol);
            for (int i = 0; i < 7 - symbol.length(); i++) {
                buffer.writeUint8(UByte.valueOf(0));
            }
        } else if (object instanceof Price) {
            Price price = (Price) object;
            serialize(buffer, price.base);
            serialize(buffer, price.quote);
        } else if (object instanceof Authority) {
            Authority authority = (Authority) object;
            serialize(buffer, authority.weight_threshold);
            serialize(buffer, authority.account_auths);
            serialize(buffer, authority.key_auths);
        } else if (object instanceof Transaction) {
            Transaction transaction = (Transaction) object;
            serialize(buffer, transaction.ref_block_num);
            serialize(buffer, transaction.ref_block_prefix);
            serialize(buffer, transaction.expiration);
            serialize(buffer, transaction.operations);
            serialize(buffer, transaction.extensions);
        } else if (object instanceof FutureExtensions) {
            // not supported, do nothing
        }
    }

    private static class UByteTypeAdapter implements JsonDeserializer<UByte>, JsonSerializer<UByte> {
        @Override
        public UByte deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return UByte.valueOf(json.getAsLong());
            } catch (Exception e) {
                throw new JsonParseException(e);
            }
        }

        @Override
        public JsonElement serialize(UByte src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.longValue());
        }
    }

    private static class UShortTypeAdapter implements JsonDeserializer<UShort>, JsonSerializer<UShort> {
        @Override
        public UShort deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return UShort.valueOf(json.getAsInt());
            } catch (Exception e) {
                throw new JsonParseException(e);
            }
        }

        @Override
        public JsonElement serialize(UShort src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.longValue());
        }
    }

    private static class UIntegerTypeAdapter implements JsonDeserializer<UInteger>, JsonSerializer<UInteger> {
        @Override
        public UInteger deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return UInteger.valueOf(json.getAsLong());
            } catch (Exception e) {
                throw new JsonParseException(e);
            }
        }

        @Override
        public JsonElement serialize(UInteger src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.longValue());
        }
    }

    private static class ULongTypeAdapter implements JsonDeserializer<ULong>, JsonSerializer<ULong> {
        @Override
        public ULong deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return ULong.valueOf(json.getAsLong());
            } catch (Exception e) {
                throw new JsonParseException(e);
            }
        }

        @Override
        public JsonElement serialize(ULong src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.longValue());
        }
    }

    private static class PublicKeyTypeAdapter implements JsonDeserializer<PublicKey>, JsonSerializer<PublicKey> {
        @Override
        public PublicKey deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return PublicKey.fromString(json.getAsString());
            } catch (Exception e) {
                throw new JsonParseException(e);
            }
        }

        @Override
        public JsonElement serialize(PublicKey src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }

    private static class TimePointSecTypeAdapter implements JsonDeserializer<TimePointSec>, JsonSerializer<TimePointSec> {
        @Override
        public TimePointSec deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return TimePointSec.fromString(json.getAsString());
            } catch (Exception e) {
                throw new JsonParseException(e);
            }
        }

        @Override
        public JsonElement serialize(TimePointSec src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }

    private static class AssetTypeAdapter implements JsonDeserializer<Asset>, JsonSerializer<Asset> {
        @Override
        public Asset deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return Asset.fromString(json.getAsString());
            } catch (Exception e) {
                throw new JsonParseException(e);
            }
        }

        @Override
        public JsonElement serialize(Asset src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }

    private static class Ripemd160TypeAdapter implements JsonDeserializer<Ripemd160>, JsonSerializer<Ripemd160> {
        @Override
        public Ripemd160 deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return Ripemd160.fromString(json.getAsString());
            } catch (Exception e) {
                throw new JsonParseException(e);
            }
        }

        @Override
        public JsonElement serialize(Ripemd160 src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }

    private static class SignatureTypeAdapter implements JsonDeserializer<Signature>, JsonSerializer<Signature> {
        @Override
        public Signature deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return Signature.fromString(json.getAsString());
            } catch (Exception e) {
                throw new JsonParseException(e);
            }
        }

        @Override
        public JsonElement serialize(Signature src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }

    private static class OptionalTypeAdapter implements JsonDeserializer<Optional>, JsonSerializer<Optional> {
        @Override
        public Optional deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return new Optional(context.deserialize(json, Object.class));
            } catch (Exception e) {
                throw new JsonParseException(e);
            }
        }

        @Override
        public JsonElement serialize(Optional src, Type typeOfSrc, JsonSerializationContext context) {
            return context.serialize(src.value);
        }
    }

    private static class ArrayHashMapTypeAdapter implements JsonSerializer<ArrayHashMap<Object, Object>> {
        @Override
        public JsonElement serialize(ArrayHashMap<Object, Object> src, Type typeOfSrc, JsonSerializationContext context) {
            ArrayList<ArrayList<Object>> result = new ArrayList<>();
            for (Map.Entry<Object, Object> item : src.entrySet()) {
                ArrayList<Object> list = new ArrayList<>();
                list.add(item.getKey());
                list.add(item.getValue());
                result.add(list);
            }
            return context.serialize(result);
        }
    }

    private static class OperationTypeAdapter implements JsonDeserializer<Operation>, JsonSerializer<Operation> {
        @Override
        public Operation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                ArrayList<Object> list = context.deserialize(json, ArrayList.class);
                LinkedTreeMap<String, Object> map = (LinkedTreeMap) list.get(1);
                LinkedHashMap<String, Object> params = new LinkedHashMap<>();
                for (Map.Entry<String, Object> item : map.entrySet()) {
                    params.put(item.getKey(), item.getValue());
                }
                return new Operation((String) list.get(0), params);
            } catch (Exception e) {
                throw new JsonParseException(e);
            }
        }

        @Override
        public JsonElement serialize(Operation src, Type typeOfSrc, JsonSerializationContext context) {
            ArrayList<Object> list = new ArrayList<>();
            list.add(src.name);
            list.add(src.params);
            return context.serialize(list);
        }
    }
}
