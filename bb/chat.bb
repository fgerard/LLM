#!/usr/bin/env bb

(ns sequencia 
  (:require [babashka.curl :as curl]
            [cheshire.core :refer :all]
            [clojure.pprint :as pp]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :as set]))



(let [prompt  *command-line-args*
      prompt (str/join " " prompt)
      ctx-file (->> "context.edn" (io/file))
      ctx (if (not (.exists ctx-file)) [] (->> ctx-file slurp read-string)) 
      ask {:model "llama3", :prompt prompt, :context ctx, :stream false}]
    ;(pp/pprint ask)
    (let [resp (curl/post "http://localhost:11434/api/generate" 
                          {:body (generate-string ask)
                           :headers {"Content-Type" "application/json"}})
          body (parse-string (:body resp) keyword)
          {:keys [response context]} body]
      ;(println context)
      (spit ctx-file context)
      (println prompt)
      (println response)
      (println)
      ))
