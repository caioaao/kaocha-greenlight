(ns caioaao.kaocha-greenlight.report
  (:require [clojure.test :as ctest]
            [greenlight.report]
            [clojure.string :as str]
            [greenlight.test :as test]
            [greenlight.step :as step]))

(def ^:private sgr-code
  "Map of symbols to numeric SGR (select graphic rendition) codes."
  {:none      0
   :bold      1
   :underline 3
   :black     30
   :red       31
   :green     32
   :yellow    33
   :blue      34
   :magenta   35
   :cyan      36
   :white     37})

(defn- sgr
  "Returns an ANSI escope string which will apply the given collection of SGR
  codes."
  [codes]
  (let [codes (map sgr-code codes codes)
        codes (str/join \; codes)]
    (str \u001b \[ codes \m)))

(defn- color
  [codes string]
  (let [codes (if (coll? codes) codes [codes])]
    (str (sgr codes) string (sgr [:none]))))

(defn- state-color
  "Return the color keyword for the given outcome state."
  [outcome]
  (case outcome
    :pass    :green
    :fail    :red
    :error   :red
    :timeout :yellow
    :magenta))

(defmethod ctest/report :test-start
  [event]
  (let [test-case (:test event)]
    (printf "\n %s Testing %s\n"
            (color :magenta "*")
            (color [:bold :magenta] (::test/title test-case)))
    (when (::test/ns test-case)
      (printf " %s %s:%s\n"
              (color :magenta "|")
              (::test/ns test-case)
              (::test/line test-case -1)))
    (when-let [desc (::test/description test-case)]
      (printf " %s %s\n"
              (color :magenta "|")
              (color :yellow (::test/description test-case))))
    (printf " %s\n" (color :magenta "|"))))

(defmethod ctest/report :step-start
  [event]
  (let [step (:step event)]
    (printf " %s %s\n"
            (color :blue "+->>")
            (::step/title step))))

(defmethod ctest/report :step-end
  [event]
  (let [result (:step event)]
    (when-let [message (::step/message result)]
      (printf " %s %s\n"
              (color :blue "|")
              (color (state-color (::step/outcome result))
                     (::step/message result))))
    (printf " %s [%s] (%s seconds)\n"
            (color :blue "|")
            (let [outcome (::step/outcome result "???")]
              (color
               [:bold (state-color outcome)]
               (str/upper-case (name outcome))))
            (color :cyan (format "%.3f" (::step/elapsed result))))
    (printf " %s\n" (color :blue "|"))))

(defmethod ctest/report :cleanup-resource
  [event]
  (let [{:keys [resource-type parameters]} event]
    (printf " %s Cleaning %s resource %s\n"
            (color :yellow "+->>")
            (color [:bold :yellow] resource-type)
            (color :magenta (pr-str parameters)))))

(defmethod ctest/report :cleanup-error
  [event]
  (let [{:keys [error resource-type parameters]} event]
    (printf " %s Failed to cleanup %s resource %s: %s\n"
            (color :yellow "|")
            (color [:bold :yellow] resource-type)
            (color :magenta (pr-str parameters))
            (color :red error))))

(defmethod ctest/report :test-end
  [event]
  (let [result  (:test event)
        outcome (::test/outcome result "---")
        codes   [:bold (state-color outcome)]]
    (printf " %s\n" (color :blue "|"))
    (printf " %s (%s seconds)\n"
            (color codes (str "* " (str/upper-case (name outcome))))
            (color :cyan (format "%.3f" (test/elapsed result))))
    (when-let [message (::test/message result)]
      (printf "   %s\n" message))
    (newline)))
