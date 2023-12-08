package com.universestay.project.room.controller;

import com.universestay.project.room.dto.RoomDto;
import com.universestay.project.room.dto.RoomImgDto;
import com.universestay.project.room.service.RoomService;
import com.universestay.project.user.dao.UserWithdrawalDao;
import com.universestay.project.user.dto.UserDto;
import com.universestay.project.user.service.ProfileImgServiceImpl;
import com.universestay.project.user.service.UserInfoService;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/room")
public class RoomController {

    @Autowired
    RoomService roomService;
    @Autowired
    UserWithdrawalDao userWithdrawalDao;
    @Autowired
    ProfileImgServiceImpl profileImgService;

    @Autowired
    UserInfoService userInfoService;

    @GetMapping("")
    public String showRoom() {
        return "/room/roomDetail_copy";
    }

    @GetMapping("/{room_id}")
    public String lookUpRoom(@PathVariable String room_id, Model model) {
        try {
            RoomDto room = roomService.lookUpRoom(room_id);
            List<RoomImgDto> roomImgs = roomService.lookUp5RoomImg(room_id);
            UserDto host = userWithdrawalDao.selectUserByUuid(room.getUser_id());
            String profileImgUrl = profileImgService.getProfileImgUrl(room.getUser_id());

            if (room == null) {
                // TODO: 에러메세지 보여주고 메인으로 이동
                return "main/main";
            }

            model.addAttribute("room", room);
            model.addAttribute("roomImgList", roomImgs);
            model.addAttribute("host", host);
            model.addAttribute("profileImgUrl", profileImgUrl);

            return "room/roomDetail";
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: 에러메세지 보여주고 메인으로 이동
            return "main/main";
        }
    }

    @GetMapping("/roomDelete")
    public String roomDelete() {
        return "/room/roomDelete";
    }

    @GetMapping({"/category/{categoryOrView}", "/view/{categoryOrView}"})
    public String lookUpRoomByCategoryOrView(@PathVariable String categoryOrView, Model model,
            HttpSession session) throws Exception {
        String userEmail = (String) session.getAttribute("user_email");

        UserDto user = userInfoService.getUserInfo(userEmail);
        if (userEmail != null) {
            String profileImgUrl = profileImgService.getProfileImgUrl(user.getUser_id());
            String isHost = user.getUser_is_host();

            model.addAttribute("user", user);
            model.addAttribute("profileImgUrl", profileImgUrl);
            model.addAttribute("isHost", isHost);
        }

        List<?> roomList;

        //들어온 PathVariable이 RC를 포함하면 Category를 통해 검색하는 메소드를 호출 (ex. RC01, RC02..)
        //들어온 PathVariable이 RV를 포함하면 View를 통해 검색하는 메소드 호출 (ex. RV01, RV02..)
        if (categoryOrView != null && categoryOrView.contains("RC")) {
            roomList = roomService.lookUpAllRoomByCategory(categoryOrView);
        } else if (categoryOrView != null && categoryOrView.contains("RV")) {
            roomList = roomService.lookUpAllRoomByView(categoryOrView);
        } else {
            return "main/main";
        }

        model.addAttribute("roomList", roomList);
        return "main/main";
    }

}
