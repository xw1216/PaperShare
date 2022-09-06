package tech.outspace.papershare.utils.encrypt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import tech.outspace.papershare.model.vo.TokenVo;
import tech.outspace.papershare.utils.time.TimeUtil;

import java.time.LocalDateTime;

public class JwtUtil {
    private static final String userStr = "userId";
    private static final String sessionStr = "sessionId";
    private static final String issuerStr = "PaperShare";

    public static String sign(TokenVo tokenVo, String secret, LocalDateTime time) {
        try {
            Algorithm algo = Algorithm.HMAC512(secret);
            return JWT
                    .create()
                    .withClaim(userStr, tokenVo.getUserId())
                    .withClaim(sessionStr, tokenVo.getSessionId())
                    .withExpiresAt(TimeUtil.getUTCDate(time))
                    .withIssuer(issuerStr)
                    .sign(algo);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public static boolean verify(TokenVo tokenVo, String secret, String jwt) {
        try {
            Algorithm algo = Algorithm.HMAC512(secret);
            JWTVerifier verifier = JWT
                    .require(algo)
                    .withClaim(userStr, tokenVo.getUserId())
                    .withClaim(sessionStr, tokenVo.getSessionId())
                    .withIssuer(issuerStr)
                    .build();
            DecodedJWT decodedJWT = verifier.verify(jwt);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static TokenVo decodePayload(DecodedJWT decodedJWT) {
        String userId = decodedJWT.getClaim(userStr).asString();
        String sessionId = decodedJWT.getClaim(sessionStr).asString();
        if (userId == null || sessionId == null) {
            return null;
        }
        return new TokenVo(userId, sessionId);
    }

}
