package oit.is.z2001.kaizi.janken.controller;

import java.security.Principal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import oit.is.z2001.kaizi.janken.model.Entry;
import oit.is.z2001.kaizi.janken.model.User;
import oit.is.z2001.kaizi.janken.model.UserMapper;
import oit.is.z2001.kaizi.janken.model.Match;
import oit.is.z2001.kaizi.janken.model.MatchMapper;

@Controller
public class JankenController {
  @Autowired
  private Entry entry;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private MatchMapper matchMapper;

  @GetMapping("/janken")
  public String janken(Principal prin, ModelMap model) {
    String loginUserName = prin.getName();
    ArrayList<User> users = userMapper.selectAllUsers();
    ArrayList<Match> matches = matchMapper.selectAllMatche();
    model.addAttribute("login_user_name", loginUserName);
    model.addAttribute("all_users", users);
    model.addAttribute("all_matches", matches);
    return "janken.html";
  }

  @PostMapping("/janken")
  public String janken(@RequestParam String name, ModelMap model) {
    model.addAttribute("name", name);
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
  public String match(Principal prin, @RequestParam Integer id, ModelMap model) {
    String loginUser = prin.getName();
    User enemy_user = userMapper.selectUserById(id);
    model.addAttribute("user_id", id);
    model.addAttribute("login_user_name", loginUser);
    model.addAttribute("enemy_user", enemy_user);
    return "match.html";
  }

  @GetMapping("/fight")
  public String fight(@RequestParam Integer id, @RequestParam Integer enemy_id,
      @RequestParam String user_hand,
      ModelMap model) {
    String hand_list[] = { "Gu", "Choki", "Pa" };
    String enemy_hand = hand_list[(int) (Math.random() * 3)];
    String result = "";
    User enemy_user = userMapper.selectUserById(enemy_id);
    Match match = new Match();
    match.setUser1(id.toString());
    match.setUser2(enemy_id.toString());
    match.setUser1Hand(user_hand);
    match.setUser2Hand(enemy_hand);
    matchMapper.insertMatch(match);

    if (user_hand.equals(enemy_hand)) {
      result = "Draw";
    } else if (user_hand.equals("Gu") && enemy_hand.equals("Choki")
        || user_hand.equals("Choki") && enemy_hand.equals("Pa")
        || user_hand.equals("Pa") && enemy_hand.equals("Gu")) {
      result = "Win";
    } else {
      result = "Lose";
    }

    model.addAttribute("user_id", id);
    model.addAttribute("enemy_user", enemy_user);
    model.addAttribute("your_hand", user_hand);
    model.addAttribute("enemy_hand", enemy_hand);
    model.addAttribute("result", result);

    return "match.html";
  }
}
