(ns clj-irc-cli.core
  (:require [clojure.core.async :refer [go-loop]]
            [aleph.tcp :refer [tcp-client]]
            [lamina.core :refer [enqueue wait-for-message wait-for-result]]
            [gloss.core :refer [string]]))

(let [ch (wait-for-result
          (tcp-client {:host "irc.freenode.net",
                       :port 6667,
                       :frame (string :utf-8 :delimiters ["\r\n"])}))]
  
  (enqueue ch "NICK clojure-user")
  (enqueue ch "USER clojure-user 8 *  : Jonny Blogger")
  (enqueue ch "JOIN #clojure")
  
  (go-loop []
    (when-let [msg (wait-for-message ch)]
      (when (.contains msg "PING")
        (enqueue ch "PONG"))
      (when (.contains msg "PRIVMSG #clojure")
        (println (str
                  (second (re-matches #":([a-zA-Z]+).*" msg))
                  " says: "
                  (second (re-matches #".*PRIVMSG #clojure :(.*)" msg)))))
      (recur)))
  
  (loop []
    (let [input (read-line)]
      (enqueue ch (str "PRIVMSG #clojure :" input))
      (recur))))
