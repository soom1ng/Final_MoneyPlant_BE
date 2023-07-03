package com.MoneyPlant.service;

import com.MoneyPlant.dto.MyWorkDto;
import com.MoneyPlant.entity.*;
import com.MoneyPlant.repository.*;
import com.MoneyPlant.service.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import javax.transaction.Transactional;
import java.util.List;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import javax.security.auth.kerberos.KerberosKey;
//import javax.servlet.http.HttpServletRequest;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MyWorkService {
    private final MyWorkRepository myWorkRepository;
    private final UserRepository userRepository;

    // 마이페이지 나의 근무 생성
    public boolean createMyWork(MyWorkDto myWorkDto, UserDetailsImpl userDetails) {
        try {
            Long userId = userDetails.getId();
            myWorkDto.setUserId(userId);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            MyWork myWork = new MyWork();
            myWork.setUser(user);
            myWork.setMyWorkName(myWorkDto.getMyWorkName());
            myWork.setMyColor(myWorkDto.getMyColor());
            myWork.setMyPayType(myWorkDto.getMyPayType());
            myWork.setMyWorkMoney(myWorkDto.getMyWorkMoney());
            myWork.setMyWorkTime(myWorkDto.getMyWorkTime());
            myWork.setMyWorkTax(myWorkDto.getMyWorkTax());
            myWork.setMyWorkPay(myWorkDto.getMyWorkPay());
            myWork.setMyPayDay(myWorkDto.getMyPayDay());

            myWorkRepository.save(myWork);
            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    // 마이페이지 나의 근무 수정

    // 마이페이지 나의 근무 삭제

    // ===========================================================================
    // 마이페이지 전체 나의 근무 조회
    public List<MyWorkDto> getWorkForMyPage(UserDetailsImpl userDetails) {
        Long userId = userDetails.getId();
        List<MyWork> myWorkList = myWorkRepository.findByUserId(userId);

        List<MyWorkDto> myWorkDtoList = new ArrayList<>();
        for (MyWork myWork : myWorkList) {
            MyWorkDto myWorkDto = new MyWorkDto();

            // 조회 내용 : 근무 이름, 근무 색
            myWorkDto.setMyWorkName(myWork.getMyWorkName());
            myWorkDto.setMyColor(myWork.getMyColor());
            myWorkDto.setMyPayType(myWork.getMyPayType());
            myWorkDto.setMyWorkMoney(myWork.getMyWorkMoney());
            myWorkDto.setMyWorkTime(myWork.getMyWorkTime());
            myWorkDto.setMyWorkTax(myWork.getMyWorkTax());
            myWorkDto.setMyWorkPay(myWork.getMyWorkPay());
            myWorkDto.setMyPayDay(myWork.getMyPayDay());

            myWorkDtoList.add(myWorkDto);
        }
        return myWorkDtoList;
    }
}
