/*
 * Copyright 2011-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lambdaworks.redis.protocol;

import com.lambdaworks.redis.RedisException;
import com.lambdaworks.redis.ScriptOutputType;
import com.lambdaworks.redis.codec.RedisCodec;
import com.lambdaworks.redis.output.*;

/**
 * @author Mark Paluch
 * @since 3.0
 */
public class BaseRedisCommandBuilder<K, V> {

    protected final RedisCodec<K, V> codec;

    public BaseRedisCommandBuilder(RedisCodec<K, V> codec) {
        this.codec = codec;
    }

    protected <T> Command<K, V, T> createCommand(CommandType type, CommandOutput<K, V, T> output) {
        return createCommand(type, output, (CommandArgs<K, V>) null);
    }

    protected <T> Command<K, V, T> createCommand(CommandType type, CommandOutput<K, V, T> output, K key) {
        CommandArgs<K, V> args = new CommandArgs<K, V>(codec).addKey(key);
        return createCommand(type, output, args);
    }

    protected <T> Command<K, V, T> createCommand(CommandType type, CommandOutput<K, V, T> output, K key, V value) {
        CommandArgs<K, V> args = new CommandArgs<K, V>(codec).addKey(key).addValue(value);
        return createCommand(type, output, args);
    }

    protected <T> Command<K, V, T> createCommand(CommandType type, CommandOutput<K, V, T> output, K key, V[] values) {
        CommandArgs<K, V> args = new CommandArgs<K, V>(codec).addKey(key).addValues(values);
        return createCommand(type, output, args);
    }

    protected <T> Command<K, V, T> createCommand(CommandType type, CommandOutput<K, V, T> output, CommandArgs<K, V> args) {
        return new Command<K, V, T>(type, output, args);
    }

    @SuppressWarnings("unchecked")
    protected <T> CommandOutput<K, V, T> newScriptOutput(RedisCodec<K, V> codec, ScriptOutputType type) {
        switch (type) {
            case BOOLEAN:
                return (CommandOutput<K, V, T>) new BooleanOutput<K, V>(codec);
            case INTEGER:
                return (CommandOutput<K, V, T>) new IntegerOutput<K, V>(codec);
            case STATUS:
                return (CommandOutput<K, V, T>) new StatusOutput<K, V>(codec);
            case MULTI:
                return (CommandOutput<K, V, T>) new NestedMultiOutput<K, V>(codec);
            case VALUE:
                return (CommandOutput<K, V, T>) new ValueOutput<K, V>(codec);
            default:
                throw new RedisException("Unsupported script output type");
        }
    }
}
