package com.example.social.media.Dto.Friends;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RemoveFriendRequest {
    UUID friendId;
}
