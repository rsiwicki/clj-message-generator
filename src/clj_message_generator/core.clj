(ns clj-message-generator.core
  (:require [langohr.core    :as rmq]
          [langohr.channel :as lch]
          [langohr.queue :as lq]
          [langohr.consumers :as lc]
          [langohr.basic :as lb]))

(def ^{:const true}
    default-exchange-name "")

(defn -main
    [& args]
    (print "starting emitter with args " args)
    (let [conn (rmq/connect)
                  ch (lch/open conn)
                  qname (nth args 0)]
          (println (format "[main] Connected. Channel id: %d" (.getChannelNumber ch)))
          (lq/declare ch qname :exclusive false :durable true :auto-delete false)
          (lb/publish ch default-exchange-name qname "interesting" :content-type "text/plain" :type "storm.events")
          (Thread/sleep 500)
          (println "[main] Disconnecting...")
          (rmq/close ch)
          (rmq/close conn)))

