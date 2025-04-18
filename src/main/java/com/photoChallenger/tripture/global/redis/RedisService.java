package com.photoChallenger.tripture.global.redis;

import com.photoChallenger.tripture.domain.item.entity.Item;
import com.photoChallenger.tripture.domain.item.repository.ItemRepository;
import com.photoChallenger.tripture.domain.post.entity.Post;
import com.photoChallenger.tripture.domain.post.repository.PostRepository;
import com.photoChallenger.tripture.global.exception.item.NoSuchItemException;
import com.photoChallenger.tripture.global.exception.post.NoSuchPostException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private final PostRepository postRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @Scheduled(cron = "0 0/2 * * * *")
    public void updatePostDBFromRedis() {
        updatePostDB();
        updateItemDB();
    }

    private void updatePostDB() {
        ScanOptions options = ScanOptions.scanOptions()
                .match("post:*")
                .count(10)
                .build();
        Cursor<byte[]> cursor = redisTemplate.executeWithStickyConnection(
                connection -> connection.scan(options)
        );
        if (cursor != null) {
            while (cursor.hasNext()) {
                String key = new String(cursor.next());
                Long postId = Long.parseLong(key.split(":")[2]);
                Post post = postRepository.findById(postId).orElseThrow(NoSuchPostException::new);

                if(key.split(":")[1].equals("view")) {
                    String view = redisTemplate.opsForValue().get(key);

                    if (view != null) {
                        post.viewCountRedis(Long.valueOf(view));
                    }
                } else if(key.split(":")[1].equals("like")) {
                    String like = redisTemplate.opsForValue().get(key);

                    if (like != null) {
                        post.likeCountRedis(Integer.valueOf(like));
                    }
                }
            }
        }
    }

    private void updateItemDB() {
        ScanOptions options = ScanOptions.scanOptions()
                .match("item:*")
                .count(10)
                .build();
        Cursor<byte[]> cursor = redisTemplate.executeWithStickyConnection(
                connection -> connection.scan(options)
        );
        if (cursor != null) {
            while (cursor.hasNext()) {
                String key = new String(cursor.next());
                Long itemId = Long.parseLong(key.split(":")[2]);
                String view = redisTemplate.opsForValue().get(key);
                if (view != null) {
                    Item item = itemRepository.findById(itemId).orElseThrow(NoSuchItemException::new);
                    item.viewCountRedis(Long.valueOf(view));
                }
            }
        }
    }
}
