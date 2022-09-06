package tech.outspace.papershare.utils.time;

import com.auth0.jwt.interfaces.DecodedJWT;
import tech.outspace.papershare.model.entity.objs.CheckCode;
import tech.outspace.papershare.model.entity.objs.Session;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

public class TimeUtil {

    public static LocalDateTime getUTC() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }

    public static Date getUTCDate(LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.of("Z")).toInstant());
    }

    public static LocalDateTime getUTCTime(Date time) {
        return time.toInstant().atZone(ZoneId.of("Z")).toLocalDateTime();
    }

    public static LocalDateTime toSystemTime(LocalDateTime time) {
        ZoneId zone = ZoneId.of("Asia/Shanghai");
        return time.atZone(zone).toLocalDateTime();
    }

    public static LocalDateTime getEmailCodeExpireTime() {
        return getUTC().plusMinutes(20);
    }

    public static LocalDateTime getSessionExpireTime() {
        return getUTC().plusMinutes(30);
    }

    public static LocalDateTime getSessionExpireTime(LocalDateTime time) {
        return time.plusHours(3);
    }

    public static LocalDateTime getJwtExpireTime(LocalDateTime time) {
        return time.plusHours(3);
    }

    public static boolean isSessionExpired(Session session, LocalDateTime time) {
        return session.getExpireTime().isBefore(time);
    }

    public static boolean isSessionExpired(Session session) {
        return session.getExpireTime().isBefore(getUTC());
    }

    public static boolean isCheckCodeExpired(CheckCode checkCode, LocalDateTime time) {
        return checkCode.getExpireTime().isBefore(time);
    }

    public static boolean isCheckCodeExpired(CheckCode checkCode) {
        return checkCode.getExpireTime().isBefore(getUTC());
    }

    public static boolean isJwtExpired(DecodedJWT decodedJWT) {
        Date time = decodedJWT.getExpiresAt();
        return getUTCTime(time).isBefore(getUTC());
    }

    public static boolean isJwtExpired(DecodedJWT decodedJWT, LocalDateTime time) {
        Date expireTime = decodedJWT.getExpiresAt();
        return getUTCTime(expireTime).isBefore(time);
    }


}
