package oit.is.z2001.kaizi.janken.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class JankenController {
  @GetMapping("/janken")
  public String janken() {
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
    // String cpu_hand = hand[(int) (Math.random() * 3)];
    String cpu_hand = hand_list[0];

    String result = result_list[0];

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
}
