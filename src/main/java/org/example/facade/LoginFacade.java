package org.example.facade;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.TimeZone;

/**
 * 门面模式 最常用的模式之一，也就是常说的封装，用于对外提供功能，隐藏内部细节
 *     将多个子系统、子组件，通过同一个门面类调度，对外提供更强大的功能
 */
public class LoginFacade {
    private final AuthComponent authComponent;
    private final SessionComponent loginComponent ;
    private final LogRecordComponent logRecordComponent;
    public LoginFacade(AuthComponent authComponent, SessionComponent loginComponent, LogRecordComponent logRecordComponent){
        this.authComponent = authComponent;
        this.loginComponent = loginComponent;
        this.logRecordComponent = logRecordComponent;
    }

    public void login(String userId){
        if(!authComponent.validUser(userId)){
            throw new RuntimeException("检验异常");
        }
        loginComponent.login(userId);
        logRecordComponent.saveLog(userId);
    }

    public static void main(String[] args) {
        LoginFacade loginFacade = new LoginFacade(new AuthComponent(), new SessionComponent(), new LogRecordComponent());
        loginFacade.login("userId");
    }
}

class AuthComponent{
    public boolean validUser(String userId){
        System.out.println("用户认证通过");
        return true;
    }
}

class SessionComponent{
    public void login(String userId){
        System.out.println("将信息存入session");
    }
}

class LogRecordComponent{
    public void saveLog(String userId){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("记录日志" +
                dateTimeFormatter.format(LocalDateTime.now(TimeZone.getTimeZone("GMT+8").toZoneId())));
    }
}
