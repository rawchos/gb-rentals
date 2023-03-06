(ns gb-rentals.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 ::active-page
 (fn [db] (:active-page db)))

(rf/reg-sub
 ::search-text
 (fn [db] (:search-text db)))

(rf/reg-sub
 ::search-results
 (fn [db] (:search-results db)))

(rf/reg-sub
 ::games-to-rent
 (fn [db] (:games-to-rent db)))
