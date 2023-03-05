(ns gb-rentals.events
  (:require [ajax.core :refer [json-response-format]]
            [day8.re-frame.http-fx]
            [gb-rentals.db :as db]
            [re-frame.core :as rf]))

;; TODO: Ideally, this would be configurable by env. Good enough for now.
(def base-url "http://localhost:3000/api/games/")

(defn find-game [db guid]
  (->> (:search-results db)
       (filter #(= guid (:guid %)))
       first))

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(rf/reg-event-fx 
 :search-games
 (fn [{:keys [db]} [_ search-text]]
   (js/console.log "searching for " search-text)
   {:http-xhrio {:method          :get
                 :uri             (str base-url "search")
                 :params          {:query search-text}
                 :response-format (json-response-format {:keywords? true})
                 :on-success      [:search-games-success]}
    :db (assoc db :search-text search-text)}))

(rf/reg-event-fx
 :search-games-success
 (fn [{:keys [db]} [_ {:keys [numberOfPageResults results]}]]
   (js/console.log "Received " numberOfPageResults " results. [" (count results) "]")
   {:dispatch [:set-active-page :search-results]
    :db (assoc db :search-results results)}))

(rf/reg-event-db
 :toggle-rental
 (fn [db [_ guid]]
   (if-let [_game (get-in db [:rented-games guid])]
     (update-in db [:rented-games] dissoc guid)
     (update-in db [:rented-games] assoc guid (find-game db guid)))))

(rf/reg-event-db
 :set-active-page
 (fn [db [_ page]]
   (assoc db :active-page page)))