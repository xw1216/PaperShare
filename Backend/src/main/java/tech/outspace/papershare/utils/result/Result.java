package tech.outspace.papershare.utils.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
    }

    public Result(EResult res) {
        if (res == null) {
            return;
        }
        this.code = res.getCode();
        this.msg = res.getMsg();
        this.data = null;
    }

    public Result(EResult res, T data) {
        if (res == null) {
            return;
        }
        this.code = res.getCode();
        this.msg = res.getMsg();
        this.data = data;
    }

    public static <T> Result<T> factory(EResult eResult, T data) {
        if (eResult == null) {
            return new Result<>();
        } else if (data == null) {
            return new Result<>(eResult.getCode(), eResult.getMsg());
        } else {
            return new Result<>(eResult.getCode(), eResult.getMsg(), data);
        }
    }

    public static <T> Result<T> factory(EResult eResult, String msg, T data) {
        if (eResult == null) {
            return new Result<>();
        } else if (data == null) {
            return new Result<>(eResult.getCode(), msg);
        } else {
            return new Result<>(eResult.getCode(), msg, data);
        }
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(EResult.SUCCESS.getCode(), EResult.SUCCESS.getMsg(), data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result<?> result = (Result<?>) o;
        return Objects.equals(code, result.code) && Objects.equals(msg, result.msg) && Objects.equals(data, result.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, msg, data);
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
