package io.sponges.dubtrack4j.util;

public enum Role {

    CO_OWNER("co-owner", "5615fa9ae596154a5c000000"),
    MANAGER("manager", "5615fd84e596150061000003"),
    MOD("mod", "52d1ce33c38a06510c000001"),
    VIP("vip", "5615fe1ee596154fc2000001"),
    RESIDENT_DJ("resident-dj", "5615feb8e596154fc2000002");

    private String name;
    private String id;

    Role(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
