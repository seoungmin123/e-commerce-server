package kr.hhplus.be.server.user.reader;

import kr.hhplus.be.server.user.domain.User;

public interface UserReader {
    User getUser(Long userId);
}