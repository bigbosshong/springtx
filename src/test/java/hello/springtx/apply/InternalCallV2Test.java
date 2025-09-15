package hello.springtx.apply;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class InternalCallV2Test {

    @Autowired
    CallService service;

    @Test
    void externalCall() {
        service.external();
    }


    @TestConfiguration
    static class InternalCallV2TestConfig {
        @Bean
        CallService callService() {
            return new CallService(internalCallService());
        }

        @Bean
        InternalCallService internalCallService() {
            return new InternalCallService();
        }
    }

    @RequiredArgsConstructor
    static class CallService {
        private final InternalCallService internalCallService;

        public void external() {
            log.info("call external");
            printTxInfo();
            internalCallService.internal();
        }

        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active={}", txActive);
        }
    }

    @Slf4j
    static class InternalCallService {

        @Transactional
        public void internal() {
            log.info("call internal");
            printTxInfo();
        }

        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active={}", txActive);
        }
    }
}
