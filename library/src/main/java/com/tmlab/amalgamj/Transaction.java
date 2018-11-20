package com.tmlab.amalgamj;

import org.joou.UInteger;
import org.joou.UShort;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class Transaction {

    public UShort ref_block_num;
    public UInteger ref_block_prefix;
    public TimePointSec expiration;
    public List<Operation> operations = new ArrayList<>();
    public Set<FutureExtensions> extensions = new HashSet<>();
    public List<Signature> signatures = new ArrayList<>();

    private static final String CHAIN_ID = "46179a8f45db072e7848bf0a478446dd257dd5ad8e16069ad9852ea280f65591";

    private static final int EXPIRATION_INTERVAL = 600; // in seconds

    public static Transaction prepare() throws Exception {
        Response response = Connection.execute("database_api", "get_dynamic_global_properties", null, null);
        response.checkError();
        DynamicGlobalProperties properties = Serializer.fromJson(response, response.getObject(), DynamicGlobalProperties.class);
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("block_num", properties.last_irreversible_block_num);
        response = Connection.execute("database_api", "get_block_header", params, null);
        response.checkError();
        BlockHeader blockHeader = Serializer.fromJson(response, response.getObjectField("header"), BlockHeader.class);
        Transaction transaction = new Transaction();
        transaction.ref_block_num = UShort.valueOf(properties.last_irreversible_block_num.subtract(1).intValue() & 0xFFFF);
        transaction.ref_block_prefix = blockHeader.previous.getPrefix();
        transaction.expiration = properties.time.add(EXPIRATION_INTERVAL);
        return transaction;
    }

    public void addOperation(Operation operation) {
        operations.add(operation);
    }

    public void sign(PrivateKey privateKey) throws Exception {
        ByteBuffer buffer = new ByteBuffer();
        Serializer.serialize(buffer, this);
        byte[] msg = Utils.concat(Utils.hex2bin(CHAIN_ID), buffer.toByteArray());
        signatures.add(Signature.sign(msg, privateKey));
    }
}
