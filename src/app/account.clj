(ns app.account
  (:require [app.domain :refer :all]))

(defn bankApp [time-service]

  (def transactions (atom (vector)))

  (defn printStatement
    []
    (def header (apply str "Date || Amount || Balance\n"))

    (def txs
      (map
        (fn [tx]
          (let [{:keys [date amount balance]} tx]
            (str date " || " amount " || " balance)
          )
        )
        @transactions)
    )

    (str header (clojure.string/join "\n" (reverse txs)))
  )

  (defn latest-balance []
    (def tx-amounts
      (map
        (fn [tx] (let [{:keys [amount]} tx] amount))
        @transactions))

    (reduce + tx-amounts)
  )

  (defn deposit [amnt]
    (def date (:date @time-service))
    (def bal (+ amnt (latest-balance)))
    (def trans (->Transaction date amnt bal))
    (reset! transactions (conj @transactions trans))
  )

  (defn withdraw [amnt]
    (def date (:date @time-service))
    (def bal (- (latest-balance) amnt))
    (def trans (->Transaction date (- 0 amnt) bal))
    (reset! transactions (conj @transactions trans))
  )

  (defn showDate [acct] (:date @time-service))

  (def bankAccount
    {:showDate showDate
     :printStatement printStatement
     :deposit deposit
     :withdraw withdraw}
  )

  bankAccount
)

