package oit.is.z2001.kaizi.janken.controller;

import java.security.Principal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.z2001.kaizi.janken.model.Entry;
import oit.is.z2001.kaizi.janken.model.User;
import oit.is.z2001.kaizi.janken.model.UserMapper;
import oit.is.z2001.kaizi.janken.model.Match;
import oit.is.z2001.kaizi.janken.model.MatchMapper;
import oit.is.z2001.kaizi.janken.model.MatchInfo;
import oit.is.z2001.kaizi.janken.model.MatchInfoMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oit.is.z2001.kaizi.janken.service.AsyncKekka;

@Controller
public class JankenController {
  @Autowired
  private Entry entry;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private MatchMapper matchMapper;

  @Autowired
  private MatchInfoMapper matchInfoMapper;

  @Autowired
  private AsyncKekka asyncKekka;

  @GetMapping("/janken")
  public String janken(Principal prin, ModelMap model) {
    String loginUserName = prin.getName();
    ArrayList<User> users = userMapper.selectAllUsers();
    ArrayList<Match> matches = matchMapper.selectAllMatche();
    MatchInfo activeMatchInfo = matchInfoMapper.selectActiveMatchInfo();

    model.addAttribute("login_user_name", loginUserName);
    model.addAttribute("all_users", users);
    model.addAttribute("all_matches", matches);
    model.addAttribute("active_matches", activeMatchInfo);

    return "janken.html";
  }

  @GetMapping("/janken/entry")
  public String entry(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    this.entry.addUser(loginUser);
    model.addAttribute("entry", this.entry);

    return "janken.html";
  }

  @GetMapping("/match")
  public String match(@AuthenticationPrincipal UserDetails user, @RequestParam Integer enemy_user_id, ModelMap model) {
    User login_user = userMapper.selectUserById((userMapper.selectUserIdByName(user.getUsername())));
    User enemy_user = userMapper.selectUserById(enemy_user_id);
    model.addAttribute("login_user", login_user);
    model.addAttribute("enemy_user", enemy_user);
    return "match.html";
  }

  @GetMapping("/wait")
  public String wait(@RequestParam Integer login_user_id, @RequestParam Integer enemy_user_id,
      @RequestParam String login_user_hand, ModelMap model) {
    User login_user = userMapper.selectUserById(login_user_id);
    User enemy_user = userMapper.selectUserById(enemy_user_id);
    MatchInfo activeMatchInfo = matchInfoMapper.selectActiveMatchInfoByUser2(login_user_id.toString());

    if (activeMatchInfo == null) {
      // マッチ待機情報をDBに登録
      MatchInfo matchInfo = new MatchInfo();
      matchInfo.setUser1(login_user_id.toString());
      matchInfo.setUser2(enemy_user_id.toString());
      matchInfo.setUser1Hand(login_user_hand);
      matchInfo.setIsActive("TRUE");
      matchInfoMapper.insertMatchInfo(matchInfo);
    } else {
      Match match = new Match();
      match.setUser1(enemy_user_id.toString());
      match.setUser2(login_user_id.toString());
      match.setUser1Hand(activeMatchInfo.getUser1Hand());
      match.setUser2Hand(login_user_hand);
      this.asyncKekka.syncResult(match);
      activeMatchInfo.setIsActive("FALSE");
      matchInfoMapper.updateMatchInfo(activeMatchInfo);
      model.addAttribute("result", match);
    }

    model.addAttribute("login_user", login_user);
    model.addAttribute("enemy_user", enemy_user);
    model.addAttribute("login_user_hand", login_user_hand);
    return "wait.html";
  }

  @GetMapping("/janken/result")
  public SseEmitter result() {
    final SseEmitter sseEmitter = new SseEmitter();
    this.asyncKekka.result(sseEmitter);
    return sseEmitter;
  }
}
