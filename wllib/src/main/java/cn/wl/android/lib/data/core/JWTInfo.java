package cn.wl.android.lib.data.core;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Created by ace on 2017/9/10.
 */
public class JWTInfo implements Serializable, IJWTInfo {

    private String username;
    private String id;
    private String name;
    private String loginInfo;
    private List<String> deptIds;
    private String token;

    public JWTInfo(String username, String id, String name, String loginInfo, List<String> deptIds, String token) {
        this.username = username;
        this.id = id;
        this.name = name;
        this.loginInfo = loginInfo;
        this.deptIds = deptIds;
        this.token = token;
    }

    public JWTInfo() {
    }

    @Override
    public String getUniqueName() {
        return username;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getDeptIds() {
        return deptIds;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JWTInfo jwtInfo = (JWTInfo) o;

        if (!Objects.equals(username, jwtInfo.username)) {
            return false;
        }
        return Objects.equals(id, jwtInfo.id);

    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}
