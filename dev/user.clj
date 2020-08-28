(ns user
  (:require
   [clojure.spec.alpha :as s]
   [expound.alpha :as expound]
   [kaocha.repl]))

;; Sets up the expound printer for human-readable spec error messages. kaocha
;; requires this to be configured for generative fdef testing.


;; Alter the root *explain-out* binding...
(alter-var-root #'s/*explain-out* (constantly expound/printer))

(try
  ;; ...and attempt to set *explain-out*, assuming that we're inside of
  ;; a binding context, which is necessary for some REPL configurations...
  (set! s/*explain-out* expound/printer)

  (catch IllegalStateException _
    ;; ...however, we might not be inside a binding context. If not, Clojure
    ;; will throw an IllegalStateException, which is why we're catching it and
    ;; ignoring it here.
))
