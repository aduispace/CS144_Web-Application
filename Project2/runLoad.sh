#!/bin/bash

ant
ant run-all

mysql CS144 < drop.sql
mysql CS144 < create.sql
mysql CS144 < load.sql

rm *.dat
