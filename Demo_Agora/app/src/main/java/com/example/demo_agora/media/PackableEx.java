package com.example.demo_agora.media;

public interface PackableEx extends Packable {
    void unmarshal(ByteBuf in);
}
