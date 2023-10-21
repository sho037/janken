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
    model.addAttribute("all_users", userMapper.selectAllUsers());
    ArrayList<Match> matches = matchMapper.selectAllMatche();
    model.addAttribute("all_matches", matches);
    model.addAttribute("login_user_name", loginUserName);
    return "janken.html";
  }

  @PostMapping("/janken")
  public String janken(@RequestParam String name, ModelMap model) {
    model.addAttribute("name", name);
    return "janken.html";
  }

  @PostMapping("/janken/start")
  public String jankenStart(@RequestParam String player_hand, ModelMap model) {
    String[] hand_list = { "グー", "チョキ", "パー" };
    String[] result_list = { "あいこ", "勝ち", "負け" };
    String cpu_hand = hand_list[(int) (Math.random() * 3)];
    String result = "";

    if (player_hand.equals("グー")) {
      if (cpu_hand.equals("グー")) {
        result = result_list[0];
      } else if (cpu_hand.equals("チョキ")) {
        result = result_list[1];
      } else if (cpu_hand.equals("パー")) {
        result = result_list[2];
      }
    } else if (player_hand.equals("チョキ")) {
      if (cpu_hand.equals("グー")) {
        result = result_list[2];
      } else if (cpu_hand.equals("チョキ")) {
        result = result_list[0];
      } else if (cpu_hand.equals("パー")) {
        result = result_list[1];
      }
    } else if (player_hand.equals("パー")) {
      if (cpu_hand.equals("グー")) {
        result = result_list[1];
      } else if (cpu_hand.equals("チョキ")) {
        result = result_list[2];
      } else if (cpu_hand.equals("パー")) {
        result = result_list[0];
      }
    }

    model.addAttribute("player_hand", player_hand);
    model.addAttribute("cpu_hand", cpu_hand);
    model.addAttribute("result", result);

    return "janken.html";
  }

  @GetMapping("/janken/entry")
  public String entry(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    this.entry.addUser(loginUser);
    model.addAttribute("entry", this.entry);

    return "janken.html";
  }
}
