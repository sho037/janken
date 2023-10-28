package oit.is.z2001.kaizi.janken.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
  @Select("SELECT id, name from users;")
  ArrayList<User> selectAllUsers();

  @Select("SELECT id, name from users where id = #{id}")
  User selectUserById(int id);

  @Select("SELECT name from users where id = #{id}")
  String selectUserNameById(int id);

  @Select("SELECT id from users where name = #{name}")
  int selectUserIdByName(String name);
}
