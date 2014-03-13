(ns clj-irc-cli.core
  (:require [clojure.core.async :refer [go-loop]]
            [aleph.tcp :refer [tcp-client]]
            [lamina.core :refer [enqueue wait-for-message wait-for-result]]
            [gloss.core :refer [string]]))

(let [ch (wait-for-result
          (tcp-client {:host "irc.freenode.net",
                       :port 6667,
                       :frame (string :utf-8 :delimiters ["\r\n"])}))]
  
  (enqueue ch "NICK k3ny")
  (enqueue ch "USER k3ny 8 *  : Paul Mutton")
  (enqueue ch "JOIN #abc123thisisatest")
  
  (go-loop []
    (when-let [msg (wait-for-message ch)]
      (when (.contains msg "PING")
        (enqueue ch "PONG"))
      (when (.contains msg "PRIVMSG #abc123thisisatest")
        (println (str
                  (second (re-matches #":([a-zA-Z]+).*" msg))
                  " says: "
                  (second (re-matches #".*PRIVMSG #abc123thisisatest :(.*)" msg)))))
      (recur)))
  
  (loop []
    (let [input (read-line)]
      (enqueue ch (str "PRIVMSG #abc123thisisatest :" input))
      (recur))))
