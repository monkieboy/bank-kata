(ns app.account-test
  (:require [clojure.test :refer :all]
            [app.account :refer :all]))

(def ts (atom {:date "19/11/2019"}))
(defn change-date [date] (reset! ts {:date date} ))
(defn new-acct [] (bankApp ts))

(deftest printing-account-statement
  (def acct (bankApp (new-acct)))
  (testing "Statement did not include correct header."
    (is (= "Date || Amount || Balance\n" ((:printStatement acct))))))

(deftest depositing-to-account

  (def acct (new-acct))
  (def expected (str "Date || Amount || Balance\n01/11/2019 || 200 || 300\n19/11/2019 || 100 || 100" ))

  (change-date "19/11/2019")

  ((:deposit acct) 100)

  (change-date "01/11/2019")

  ((:deposit acct) 200)

  (testing "Depositing 100 to the account failed."
    (is (= expected ((:printStatement acct)))))
)

(deftest withdrawing-from-account

  (def expected (str "Date || Amount || Balance\n02/11/2019 || -100 || 200\n01/11/2019 || 200 || 300\n19/11/2019 || 100 || 100" ))
  (def acct (new-acct))

  (change-date "19/11/2019")

  ((:deposit acct) 100)

  (change-date "01/11/2019")

  ((:deposit acct) 200)

  (change-date "02/11/2019")

  ((:withdraw acct) 100)

  (testing "Withdrawing 100 from the account failed."
    (is (= expected ((:printStatement acct)))))
  )

(deftest base-case-supplied-in-kata
  (def expected (str "Date || Amount || Balance\n14/01/2012 || -500 || 2500\n13/01/2012 || 2000 || 3000\n10/01/2012 || 1000 || 1000"))
  (def acct (new-acct))

  (change-date "10/01/2012")

  ((:deposit acct) 1000)

  (change-date "13/01/2012")

  ((:deposit acct) 2000)

  (change-date "14/01/2012")

  ((:withdraw acct) 500)

  (println (str "statement: " (:printStatement acct)))

  (testing "Accounting for transactions was incorrect."
    (is (= expected ((:printStatement acct)))))
)

