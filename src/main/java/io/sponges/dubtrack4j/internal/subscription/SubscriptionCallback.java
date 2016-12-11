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

package io.sponges.dubtrack4j.internal.subscription;

import io.ably.lib.realtime.Channel;
import io.ably.lib.types.Message;
import io.sponges.dubtrack4j.internal.DubtrackAPIImpl;
import io.sponges.dubtrack4j.internal.subscription.callback.*;
import io.sponges.dubtrack4j.util.Logger;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SubscriptionCallback implements Channel.MessageListener {

    private final DubtrackAPIImpl dubtrack;
    private final Map<String, SubCallback> callbacks;

    SubscriptionCallback(DubtrackAPIImpl dubtrack) {
        Logger.debug("Subscribed!");

        this.dubtrack = dubtrack;
        this.callbacks = new HashMap<String, SubCallback>() {{
            put("chat-message", new ChatMessageCall(dubtrack));
            put("user-join", new UserJoinCall(dubtrack));
            put("user-leave", new UserLeaveCall(dubtrack));
            put("room_playlist-update", new PlaylistUpdateCall(dubtrack));
            put("room_playlist-dub", new PlaylistDubCall(dubtrack));
            put("user_update", new UserUpdateCall(dubtrack));
            put("user-kick", new UserKickCall(dubtrack));
            put("chat-skip", new ChatSkipCall(dubtrack));
        }};
    }

    @Override
    public void onMessage(Message message) {
        JSONObject json = new JSONObject(message.data.toString());
        String type = json.getString("type");

        Logger.debug(type.toUpperCase() + ": " + json.toString());

        if (type.startsWith("user_update_")) type = "user_update";

        if (!callbacks.containsKey(type)) {
            Logger.debug("Invalid callback type " + type);
            return;
        }

        try {
            callbacks.get(type).run(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
