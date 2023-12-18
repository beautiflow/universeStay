package com.universestay.project.chat.service;

import com.universestay.project.chat.dao.ChatMessageDao;
import com.universestay.project.dto.ChattingMessageDto;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    @Autowired
    private ChatMessageDao chatDao;

    @Override
    public int insertChat(ChattingMessageDto chatDto) {
        chatDto.setChat_ctt(chatDto.getChat_ctt());
        chatDto.setCreated_id(chatDto.getUser_id());
        chatDto.setUpdated_id(chatDto.getUser_id());

        return chatDao.insertChat(chatDto);
    }

    @Override
    public List<Map<String, Object>> selectChatList(String chat_room_id) {
        List<Map<String, Object>> list = chatDao.selectChatList(chat_room_id);
        return list;
    }

    @Override
    public List<ChattingMessageDto> selectFirstChatList(String chat_room_id) {
        return chatDao.selectFirstChatList(chat_room_id);
    }

}
