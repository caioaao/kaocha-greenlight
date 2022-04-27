(ns user
  (:require
   [clojure.spec.alpha :as s]
   [expound.alpha :as expound]
   [kaocha.repl]))

;; Sets up the expound printer for human-readable spec error messages. kaocha
;; requires this to be configured for generative fdef testing.

;; Alter the root *explain-out* binding.
(alter-var-root #'s/*explain-out* (constantly expound/printer))

;; Set *explain-out* if it's thread-bound, which is necessary for some REPL
;; configurations.
(when (thread-bound? #'s/*explain-out*)
  (set! s/*explain-out* expound/printer))
