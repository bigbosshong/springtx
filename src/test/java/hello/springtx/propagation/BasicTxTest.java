package hello.springtx.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import javax.sql.DataSource;

@Slf4j
@SpringBootTest
public class BasicTxTest {

    @Autowired
    PlatformTransactionManager txManager;

    @TestConfiguration
    static class Config {
        @Bean
        public PlatformTransactionManager transactionManager(DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }
    }

    @Test
    void commit() {
        log.info("--- tx start ---");
        final TransactionStatus status = txManager.getTransaction(new DefaultTransactionAttribute());

        log.info("--- tx commit start ---");
        txManager.commit(status);
        log.info("--- tx commit end ---");
    }

    @Test
    void rollback() {
        log.info("--- tx start ---");
        final TransactionStatus status = txManager.getTransaction(new DefaultTransactionAttribute());

        log.info("--- tx rollback start---");
        txManager.rollback(status);
        log.info("--- tx rollback end ---");
    }

    @Test
    void double_commit() {
        log.info("=== 첫 번째 트랜잭션 시작 ===");
        final TransactionStatus tx1 = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("=== 첫 번째 트랜잭션 커밋 시작 ===");
        txManager.commit(tx1);
        log.info("=== 첫 번째 트랜잭션 커밋 완료 ===");

        log.info("=== 두 번째 트랜잭션 시작 ===");
        final TransactionStatus tx2 = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("=== 두 번째 트랜잭션 커밋 시작 ===");
        txManager.commit(tx2);
        log.info("=== 두 번째 트랜잭션 커밋 완료 ===");
    }

    @Test
    void double_commit_rollback() {
        log.info("=== 첫 번째 트랜잭션 시작 ===");
        final TransactionStatus tx1 = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("=== 첫 번째 트랜잭션 커밋 시작 ===");
        txManager.commit(tx1);
        log.info("=== 첫 번째 트랜잭션 커밋 완료 ===");

        log.info("=== 두 번째 트랜잭션 시작 ===");
        final TransactionStatus tx2 = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("=== 두 번째 트랜잭션 롤백 시작 ===");
        txManager.rollback(tx2);
        log.info("=== 두 번째 트랜잭션 롤백 완료 ===");
    }
}
