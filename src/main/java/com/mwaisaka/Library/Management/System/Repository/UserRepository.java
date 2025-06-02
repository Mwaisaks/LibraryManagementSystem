package com.mwaisaka.Library.Management.System.Repository;

import com.mwaisaka.Library.Management.System.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
