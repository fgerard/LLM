#!/usr/bin/env bb

(ns sequencia 
  (:require [babashka.curl :as curl]
            [cheshire.core :refer :all]
            [clojure.pprint :as pp]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :as set]))



(let [[d-file & _] *command-line-args*
      ask {:model "llama3", :stream false}
      lines (line-seq (io/reader (io/file d-file)))
      ctx-file (->> "context.edn" (io/file))]
    (reduce (fn [context prompt]
              (println prompt)
              (let [ask (assoc ask :prompt prompt :context context)
                    resp (curl/post "http://localhost:11434/api/generate" 
                            {:body (generate-string ask)
                             :headers {"Content-Type" "application/json"}})
                    body (parse-string (:body resp) keyword)
                    {:keys [response context]} body]
                (println response)
                (println)
                (spit ctx-file context)
                context))
           []
           lines))
