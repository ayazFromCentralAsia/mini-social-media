package com.example.social.media.Api;


import com.example.social.media.Dto.Extra.SimpleResponse;
import com.example.social.media.Dto.Friends.AddFriendRequest;
import com.example.social.media.Dto.Friends.RemoveFriendRequest;
import com.example.social.media.Service.FriendsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friends")
public class FriendsApi {
    private final FriendsService friendsService;

    @PostMapping("/add")
    public SimpleResponse addFriend(@RequestBody AddFriendRequest id) {
        return friendsService.addFriends(id);
    }

    @DeleteMapping("/remove")
    public SimpleResponse removeFriend(@RequestBody RemoveFriendRequest id) {
        return friendsService.removeFriends(id);
    }
}
