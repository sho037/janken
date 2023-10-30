package oit.is.z2001.kaizi.janken.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.z2001.kaizi.janken.model.MatchInfoMapper;
import oit.is.z2001.kaizi.janken.model.MatchMapper;
import oit.is.z2001.kaizi.janken.model.Match;

@Service
public class AsyncKekka {
  boolean db_update = false;
  int insert_id = 0;

  @Autowired
  MatchMapper matchMapper;

  @Autowired
  MatchInfoMapper matchInfoMapper;

  @Transactional
  public void syncResult(Match match) {
    //
    this.insert_id = matchMapper.insertMatch(match);

    this.db_update = true;
  }

  public Match syncShowResult() {
    return matchMapper.selectById(this.insert_id);
  }

  @Async
  public void result(SseEmitter emitter) {
    this.db_update = true;
    try {
      while (true) {
        if (!this.db_update) {
          TimeUnit.SECONDS.sleep(1);
          continue;
        }

        Match match = this.syncShowResult();
        emitter.send(match);
        TimeUnit.SECONDS.sleep(1);
        this.db_update = false;
      }
    } catch (Exception e) {
      emitter.completeWithError(e);
    } finally {
      emitter.complete();
    }
  }
}
