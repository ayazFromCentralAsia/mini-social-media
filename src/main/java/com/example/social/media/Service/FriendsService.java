package com.example.social.media.Service;


import com.example.social.media.Dto.Extra.SimpleResponse;
import com.example.social.media.Dto.Friends.AddFriendRequest;
import com.example.social.media.Dto.Friends.RemoveFriendRequest;
import com.example.social.media.Entities.Friends;
import com.example.social.media.Entities.User;
import com.example.social.media.Repository.FriendsRepository;
import com.example.social.media.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class FriendsService {

    private final UserRepository userRepository;
    private final FriendsRepository friendsRepository;

    public SimpleResponse addFriends(AddFriendRequest friendId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByName(username).orElseThrow(() -> new RuntimeException("User not found, FriendsService.addFriends"));
        if (friendsRepository.existsByUser(user)) {
            User userToAdd = userRepository.findById(friendId.getFriendId()).orElseThrow(() -> new RuntimeException("User not found, FriendsService.addFriends"));
            Friends friends = friendsRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Friends not found, FriendsService.addFriends"));
            friends.getFriends().add(userToAdd);
            friendsRepository.save(friends);
        } else {
            Friends friends = new Friends();
            User userToAdd = userRepository.findById(friendId.getFriendId()).orElseThrow(() -> new RuntimeException("User not found, FriendsService.addFriends"));
            friends.setUser(user);
            ArrayList<User> friendsList = new ArrayList<>();
            friendsList.add(userToAdd);
            friends.setFriends(friendsList);
            friendsRepository.save(friends);
        }
        return SimpleResponse.builder().status(HttpStatus.OK).message("Friend added successfully.").build();
    }
    public SimpleResponse removeFriends(RemoveFriendRequest friendId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByName(username).orElseThrow(() -> new RuntimeException("User not found, FriendsService.removeFriends"));
        if (friendsRepository.existsByUser(user)) {
            User userToRemove = userRepository.findById(friendId.getFriendId()).orElseThrow(() -> new RuntimeException("User not found, FriendsService.removeFriends"));
            Friends friends = friendsRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Friends not found, FriendsService.removeFriends"));
            friends.getFriends().remove(userToRemove);
            friendsRepository.save(friends);
        } else {
            throw new RuntimeException("Friends not found, FriendsService.removeFriends");
        }
        return SimpleResponse.builder().status(HttpStatus.OK).message("Friend removed successfully.").build();
    }
}
