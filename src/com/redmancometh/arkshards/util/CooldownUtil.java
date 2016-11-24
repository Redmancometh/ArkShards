package com.redmancometh.arkshards.util;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class CooldownUtil
{
    /**
     * TODO: implement custom LoadingCache, and custom CacheBuilder specifically for cooldowns.
     */

    /**
     * 
     * @param seconds
     * @return
     */
    public static LoadingCache buildCooldownCache(int seconds)
    {
        return CacheBuilder.newBuilder().expireAfterAccess(seconds, TimeUnit.SECONDS).build(new CacheLoader<UUID, Boolean>()
        {
            @Override
            public Boolean load(UUID key)
            {
                return false;
            }
        });
    }

    public static LoadingCache buildCooldownCache(TimeUnit units, int seconds)
    {
        return CacheBuilder.newBuilder().expireAfterAccess(seconds, units).build(new CacheLoader<UUID, Boolean>()
        {
            @Override
            public Boolean load(UUID key)
            {
                return false;
            }
        });
    }
}
