package oit.is.z2001.kaizi.janken.model;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MatchInfoMapper {
  @Insert("INSERT INTO matchinfo (user1, user2, user1Hand, isActive) VALUES (#{user1},#{user2},#{user1Hand},#{isActive});")
  void insertMatchInfo(MatchInfo matchInfo);

  @Select("SELECT id, user1, user2, isActive from matchinfo where isActive = true;")
  MatchInfo selectActiveMatchInfo();
}
