(ns gb-rentals.events
  (:require [ajax.core :refer [json-response-format]]
            [day8.re-frame.http-fx]
            [gb-rentals.db :as db]
            [re-frame.core :as rf]))

; example url for searching
; https://www.giantbomb.com/api/search/?api_key=821c7e3680b2596c12e026f78ff955ce09ccad20&format=json&query=%22metroid%20prime%22&resources=game

(def base-url "https://www.giantbomb.com/api/")

(defn search-params [{:keys [api-key]} search-text]
  {:api_key   api-key
   :query     search-text
   :format    "json"
   :resources "game" })

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(rf/reg-event-fx 
 :search-games
 (fn [{:keys [db]} [_ search-text]]
   {:http-xhrio {:method          :get
                 :uri             (str base-url "search/")
                 :params          (search-params db search-text)
                 :response-format (json-response-format {:keywords? true})
                 :on-success      [:search-games-success]}
    :db (assoc db :search-text search-text)}))

(rf/reg-event-db
 :search-games-success
 (fn [db {:keys [number_of_total_results results]}]
   (js/console.log "Received " number_of_total_results " results. [" (count results) "]")
   (assoc db :search-results results)))

(rf/reg-event-fx 
 :add-api-key
 (fn [{:keys [db]} [_ api-key]]
   (js/console.log "Setting api key: " api-key)
   {:db (assoc db :api-key api-key)
    :dispatch [:set-active-page :search]}))

(rf/reg-event-db
 :set-active-page
 (fn [db [_ page]]
   (assoc db :active-page page)))