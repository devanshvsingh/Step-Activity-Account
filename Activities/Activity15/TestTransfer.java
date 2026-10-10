package Activity15;

public class TestTransfer {

    public static void main(String[] args) throws Exception {

        System.out.println(
            "============================================================"
        );
        System.out.println(
            "  ACTIVITY 15 - TRANSFER WITH DAILY LIMITS"
        );
        System.out.println(
            "============================================================"
        );

        TransferService service = new TransferService();

        // STEP 9
        Account acc1 =
            (Account) AccountFactory.createAccount(
                "SAVINGS",
                1001,
                "Rajesh Sharma",
                30,
                100000
            );

        Account acc2 =
            (Account) AccountFactory.createAccount(
                "SAVINGS",
                1002,
                "Priya Patel",
                28,
                20000
            );

        acc1.setPin(1234);

        // STEP 10
        try {

            service.transfer(
                acc1,
                acc2,
                5000,
                1234
            );

            System.out.println(
                "Transfer Rs. 5,000: SUCCESS"
                + " | acc1 = Rs. " + acc1.getBalance()
                + " | acc2 = Rs. " + acc2.getBalance()
            );

        } catch (AccountException e) {

            System.out.println(
                "Transfer failed: " + e.getMessage()
            );
        }

        // STEP 11
        try {

            service.transfer(
                acc1,
                acc2,
                100000,
                1234
            );

        } catch (InsufficientBalanceException e) {

            System.out.println(
                "Insufficient balance test: "
                + e.getMessage()
                + " [PASS]"
            );
        }

        // STEP 12
        System.out.println(
            "Daily transfer limit: Rs. "
            + acc1.getDailyTransferLimit()
        );

        while (true) {

            try {

                service.transfer(
                    acc1,
                    acc2,
                    20000,
                    1234
                );

                System.out.println(
                    "Transfer Rs. 20,000: SUCCESS"
                );

            } catch (AccountException e) {

                System.out.println(
                    "Daily limit test: "
                    + e.getMessage()
                    + " [PASS]"
                );

                break;
            }
        }

        // STEP 13
        System.out.println(
            "Today's transfer total: Rs. "
            + acc1.getDailyTransferTotal()
        );

        System.out.println(
            "Remaining daily limit: Rs. "
            + acc1.getRemainingDailyTransferLimit()
        );

        System.out.println(
            "============================================================"
        );
        System.out.println(
            "  ACTIVITY 15 TEST COMPLETED!"
        );
        System.out.println(
            "============================================================"
        );
    }
}