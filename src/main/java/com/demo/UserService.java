package main.java.com.demo;

import main.java.com.demo.util.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class UserService {
    static Scanner sc = new Scanner(System.in);
    public UserService(){
        show();
    }
    public void show(){
        Out:while(true) {
            System.out.println("1.登录\n2.注册");
            String strop = sc.nextLine();
            int choice=Integer.parseInt(strop);
            switch (choice) {
                case 1:
                    try {
                        showLogin();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break Out;
                case 2:
                    try {
                        showRegister();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break Out;
                default:
                    System.out.println("请输入正确的数字！");
            }

        }
    }
    public void showLogin() throws Exception{
        System.out.print("用户名:");
        String username = sc.nextLine();
        System.out.print("密码:");
        String password = sc.nextLine();
        String sql = "select * from user_informations where username=? and password=?";
        Connection conn=JDBCUtils.getConnection();
        PreparedStatement ps=conn.prepareStatement(sql);
        ps.setString(1,username);
        ps.setString(2,password);
        ResultSet rs=ps.executeQuery();
        if(rs.next()) {
            System.out.println("登陆成功！");
        }else{
            System.out.println("用户名或密码错误，登陆失败！");
        }
        JDBCUtils.close(conn,ps,rs);
    }
    public void showRegister() throws Exception {
        System.out.println("===== 用户注册 =====");

        while (true) {

            System.out.print("请输入用户名:");
            String username = sc.nextLine().trim();

            System.out.print("请输入密码:");
            String password = sc.nextLine();

            System.out.print("请输入手机号:");
            String phoneNumber = sc.nextLine().trim();


            if (!checkUsername(username)) {
                System.out.println("用户名不合法：不能包含空格，长度不超过50字符");
                continue;
            }

            if (checkIsExistOfUsername(username)) {
                System.out.println(" 用户名已存在，请更换");
                continue;
            }

            if (!checkPassword(password)) {
                System.out.println("密码不合法：不能包含空格，长度不超过255字符");
                continue;
            }

            if (!checkPhoneNumber(phoneNumber)) {
                System.out.println("手机号格式不正确");
                continue;
            }

            if (checkIsExistOfPhoneNumber(phoneNumber)) {
                System.out.println("该手机号已被注册");
                continue;
            }

            // 执行注册（使用try-with-resources自动关闭资源）
            String sql = "insert into user_informations(username,password,phoneNumber) values(?,?,?)";
            try (Connection conn = JDBCUtils.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, phoneNumber);

                int count = ps.executeUpdate();
                if (count > 0) {
                    System.out.println("注册成功！即将跳转到登录...");
                    showLogin();
                    break;
                } else {
                    System.out.println("注册失败，请重试");
                }

            } catch (Exception e) {
                System.out.println("注册出错：" + e.getMessage());
            }
        }
    }
    public  boolean checkIsExistOfUsername(String username) throws Exception {
        String sql = "select username from user_informations where username=?";
        Connection conn=JDBCUtils.getConnection();
        PreparedStatement ps=conn.prepareStatement(sql);
        ps.setString(1,username);
        ResultSet rs=ps.executeQuery();
        if(rs.next()) {
            JDBCUtils.close(conn,ps,rs);
            return true;
        }else{
            JDBCUtils.close(conn,ps,rs);
            return false;
        }
    }
    public boolean checkIsExistOfPhoneNumber(String phoneNumber) throws Exception {
        String sql = "select username from user_informations where phoneNumber=?";
        Connection conn=JDBCUtils.getConnection();
        PreparedStatement ps=conn.prepareStatement(sql);
        ps.setString(1,phoneNumber);
        ResultSet rs=ps.executeQuery();
        if(rs.next()) {
            JDBCUtils.close(conn,ps,rs);
            return true;
        }else{
            JDBCUtils.close(conn,ps,rs);
            return false;
        }
    }
    public boolean checkPassword(String password){
        boolean flag=true;
        if(password.length()>255){
            flag=false;
        }
        for(int i=0;i<password.length();i++){
            if(password.charAt(i)==' '){
                flag=false;
            }
        }
        return flag;
    }
    public boolean checkUsername(String username){
        boolean flag=true;
        if(username.length()>50){
            flag=false;
        }
        for(int i=0;i<username.length();i++){
            if(username.charAt(i)==' '){
                flag=false;
            }
        }
        return flag;
    }
    public boolean checkPhoneNumber(String phoneNumber){
        String regex="(?:(?:\\+|00)86)?1(?:(?:3[\\d])|(?:4[5-79])|(?:5[0-35-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\\d])|(?:9[189]))\\d{8}";
        return phoneNumber.matches(regex);
    }
}
