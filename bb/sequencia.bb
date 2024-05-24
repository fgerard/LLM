#!/usr/bin/env bb

(ns sequencia 
  (:require [babashka.curl :as curl]
            [cheshire.core :refer :all]
            [clojure.pprint :as pp]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :as set]))



(let [[prompt ctx] *command-line-args*
      ctx (read-string (or ctx "[]"))  ; (if ctx-file (->> ctx-file (io/file) (slurp) (parse-string)) [])
      ask {:model "llama3", :prompt prompt, :context ctx, :stream false}]
    ;(pp/pprint ask)
    (let [resp (curl/post "http://localhost:11434/api/generate" 
                          {:body (generate-string ask)
                           :headers {"Content-Type" "application/json"}})
          body (parse-string (:body resp) keyword)
          {:keys [response context]} body]
      (println context)
      (println)
      (println response)
      ))
