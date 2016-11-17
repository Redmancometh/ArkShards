package com.redmancometh.arkshards;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.redmancometh.arkshards.util.RemoveResult;

public class ShardManager
{
    //AtomicInteger for mutability, because otherwise the CompletableFuture value would be miserable to work with.
    private LoadingCache<UUID, CompletableFuture<AtomicInteger>> shardCache = CacheBuilder.newBuilder().build(new CacheLoader<UUID, CompletableFuture<AtomicInteger>>()
    {
        @Override
        public CompletableFuture<AtomicInteger> load(UUID key)
        {
            return ArkShards.getInstance().getDatabaseManager().getShards(key);
        }
    });

    public void purgeAndSave(UUID uuid)
    {
        try
        {
            shardCache.get(uuid).thenAccept((amount) -> ArkShards.getInstance().getDatabaseManager().setShards(uuid, amount.get()));
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }
    }

    public CompletableFuture<RemoveResult> setShards(UUID player, int amount)
    {
        //This is some pretty suspect shit tbh
        return CompletableFuture.supplyAsync(() ->
        {
            AtomicInteger shards;
            try
            {
                shards = shardCache.get(player).get();
                shards.getAndSet(amount);
                return new RemoveResult(shards.get(), true);
            }
            catch (InterruptedException | ExecutionException e)
            {
                e.printStackTrace();
                return null;
            }
        });
    }

    public CompletableFuture<RemoveResult> removeShards(UUID uuid, int amount)
    {
        //This is some pretty suspect shit tbh
        return CompletableFuture.supplyAsync(() ->
        {
            AtomicInteger shards;
            try
            {
                shards = shardCache.get(uuid).get();
                if (shards.get() < amount)
                {
                    return new RemoveResult(amount, false);
                }
                shards.getAndUpdate((initial) -> initial - amount);
                return new RemoveResult(shards.get(), true);
            }
            catch (InterruptedException | ExecutionException e)
            {
                e.printStackTrace();
                return null;
            }
        });
    }

    public CompletableFuture<Integer> addShards(UUID uuid, int amount)
    {
        return CompletableFuture.supplyAsync(() ->
        {
            try
            {
                return shardCache.get(uuid).get().updateAndGet((initial) -> initial + amount);
            }
            catch (InterruptedException | ExecutionException e)
            {
                e.printStackTrace();
            }
            return null;
        });
    }

    public CompletableFuture<AtomicInteger> getShards(UUID uuid)
    {
        try
        {
            return shardCache.get(uuid);
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
