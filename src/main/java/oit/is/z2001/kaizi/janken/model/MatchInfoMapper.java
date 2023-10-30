package oit.is.z2001.kaizi.janken.model;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MatchInfoMapper {
  @Insert("INSERT INTO matchinfo (user1, user2, user1Hand, isActive) VALUES (#{user1},#{user2},#{user1Hand},#{isActive});")
  void insertMatchInfo(MatchInfo matchInfo);

  @Select("SELECT id, user1, user2, isActive from matchinfo where isActive = true;")
  MatchInfo selectActiveMatchInfo();

  @Select("SELECT id, user1, user2, isActive from matchinfo where user1 = #{user1} and isActive = true;")
  MatchInfo selectActiveMatchInfoByUser1(String user1);

  @Select("SELECT id, user1, user2, user1Hand, isActive from matchinfo where user2 = #{user2} and isActive = true;")
  MatchInfo selectActiveMatchInfoByUser2(String user2);

  @Select("SELECT id, user1, user2, isActive from matchinfo where user1 = #{user1} or user2 = #{user2} and isActive = true;")
  MatchInfo selectActiveMatchInfoByUser(String user1, String user2);

  @Update("UPDATE matchinfo SET isActive = false WHERE id = #{id};")
  void updateMatchInfo(MatchInfo matchInfo);
}
