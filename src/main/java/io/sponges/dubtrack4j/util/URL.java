/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Joe Burnard
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.sponges.dubtrack4j.util;

/**
 * Enum containing constants for each request URL
 *
 * Why use an Enum?
 * Easy to add in new URLs, IMO it looks cleaner than a class with string constants for something like this
 */
public enum URL {

    // Auth
    AUTH("/auth/dubtrack"),
    SESSION("/auth/session"),
    TOKEN("/auth/token"),

    // Requests
    JOIN_ROOM("/room/"),
    ROOM_INFO("/room/"),
    SEND_MESSAGE("/chat/"),
    SONG_DUB("/room/%s/playlist/active/dubs"),
    SONG_INFO("/song/"),
    USER_INFO("/user/"),
    KICK_USER("/chat/kick/%s/user/%s"),
    BAN_USER("/chat/ban/%s/user/%s"),
    SKIP_SONG("/chat/skip/%s/%s"),
    ROOM_PLAYLIST("/room/%s/playlist/active"),
    QUEUE_SONG("/room/%s/playlist"),
    REMOVE_SONG("/room/%s/queue/user/%s"),
    ROOM_QUEUE("/room/%s/playlist/details"),
    PAUSE_QUEUE("/room/%s/queue/pause"),
    EDIT_ROLE("/chat/%s/%s/user/%s");

    private final String host;
    private final String path;

    URL(String path) {
        this("api.dubtrack.fm", path);
    }

    URL(String host, String path) {
        this.host = host;
        this.path = path;
    }

    @Override
    public String toString() {
        return "https://" + host + path;
    }

    public String getHost() {
        return host;
    }

    public String getPath() {
        return path;
    }
}
