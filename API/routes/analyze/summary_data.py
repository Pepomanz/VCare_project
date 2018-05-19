# -*- coding: utf-8 -*-
import pandas as pd
import numpy as np
import math
import pymysql
import json
import sys
import arrow
import dateutil
import datetime


#connect to mysql
conn = pymysql.connect(host='localhost', 
                    port=3306, 
                    user='VCare_dev', 
                    passwd='!12qwaszx?', 
                    db='VCareDB')
sensor_dict = {
                'load': 4,
                'temp': 5,
                'rpm': 12,
                'speed': 13,
                'fuel': 47,
                'mileage': 49,
                'voltage': 66,
                }
cur = conn.cursor()
tz = 'Asia/Bangkok'
##Get daily data for each sensor
def querySummaryDaily(carId):
    #get data from database
    sensorIdList = ['5', '13', '47', '49']
    #convert object to data
    query_result = {
            '5':[],
            '13':[],
            '47':[],
            '49':[],
            }
    for sensorId in sensorIdList:
        sql = "SELECT value FROM carSensor WHERE carId = " + str(carId) + " AND sensorId = " + str(sensorId) + " AND time = CURDATE() ORDER BY id"
   
        cur.execute(sql)

        for row in cur:
            query_result[sensorId].append(float(row[0]))
        
    conn.commit()

    for sensorId in sensorIdList:
        if query_result[sensorId] == []:
            result = {}
            return result
        
    result={}
    buffer = []
    for i in range(len(query_result['5'])):
        if query_result['5'][i] != 0:
            buffer.append(query_result['5'][i])
    if buffer != []:
        result['temp'] = np.array(buffer)
        result['temp'] = result['temp'].mean()
    else:
        result['temp'] = 0
    
    start_distance = 0
    final_distance = 0
    start_fuel = 0
    final_fuel = 0
    exit_loop = False
    i = len(query_result['49'])-1
    while exit_loop == False:
        if query_result['49'][i] != 0:
            final_distance = query_result['49'][i]
            exit_loop = True
        i -= 1
        
    j = 0
    exit_loop = False
    while exit_loop == False:
        if query_result['49'][j] != 0:
            start_distance = query_result['49'][j]
            exit_loop = True
        j += 1

    i = len(query_result['47'])-1
    exit_loop = False
    while exit_loop == False:
        if query_result['47'][i] != 0:
            final_fuel = query_result['47'][i]
            exit_loop = True
        i -= 1

    j = 0
    exit_loop = False
    while exit_loop == False:
        if query_result['47'][j] != 0:
            start_fuel = query_result['47'][j]
            exit_loop = True
        j += 1

    result['distance'] = final_distance - start_distance
    result['fuel_consumption'] = result['distance']/(final_fuel-start_fuel)
    
    buffer = []
    for i in range(len(query_result['13'])):
        if query_result['13'][i] != 0:
            buffer.append(query_result['13'][i])
    if buffer != []:
        result['speed'] = np.array(buffer)
        result['speed'] = result['speed'].mean()
    else:
        result['speed'] = 0
    
    return result

##Get weekly data for each sensor
def querySummaryWeekly(carId):
    #get data from database
    sensorIdList = ['5', '13', '47', '49']
    query_result = {
        '5':[],
        '13':[],
        '47':[],
        '49':[],
        }
    for sensorId in sensorIdList:
        sql = "SELECT value FROM carSensor WHERE carId = " + str(carId) + " AND sensorId = " + str(sensorId) + " AND time between date_sub(now(),INTERVAL 1 WEEK) and now() ORDER BY id"
        cur.execute(sql)
        #convert object to data

        for row in cur:
            query_result[sensorId].append(float(row[0]))
        
        conn.commit()

    for sensorId in sensorIdList:
        if query_result[sensorId] == []:
            result = {}
            return result

    result={}
    buffer = []
    for i in range(len(query_result['5'])):
        if query_result['5'][i] != 0:
            buffer.append(query_result['5'][i])
    if buffer != []:
        result['temp'] = np.array(buffer)
        result['temp'] = result['temp'].mean()
    else:
        result['temp'] = 0
    
    start_distance = 0
    final_distance = 0
    start_fuel = 0
    final_fuel = 0
    exit_loop = False
    i = len(query_result['49'])-1
    while exit_loop == False:
        if query_result['49'][i] != 0:
            final_distance = query_result['49'][i]
            exit_loop = True
        i -= 1
        
    j = 0
    exit_loop = False
    while exit_loop == False:
        if query_result['49'][j] != 0:
            start_distance = query_result['49'][j]
            exit_loop = True
        j += 1

    i = len(query_result['47'])-1
    exit_loop = False
    while exit_loop == False:
        if query_result['47'][i] != 0:
            final_fuel = query_result['47'][i]
            exit_loop = True
        i -= 1

    j = 0
    exit_loop = False
    while exit_loop == False:
        if query_result['47'][j] != 0:
            start_fuel = query_result['47'][j]
            exit_loop = True
        j += 1

    result['distance'] = final_distance - start_distance
    
    result['fuel_consumption'] = abs(result['distance']/(final_fuel-start_fuel))

    buffer = []
    for i in range(len(query_result['13'])):
        if query_result['13'][i] != 0:
            buffer.append(query_result['13'][i])
    if buffer != []:
        result['speed'] = np.array(buffer)
        result['speed'] = result['speed'].mean()
    else:
        result['speed'] = 0
    
    return result

def queryDailyData(carId, sensorId):
    #get data from database
    sql = "SELECT time, value FROM carSensor WHERE carId = " + str(carId) + " AND sensorId = " + str(sensorId) + " AND time = CURDATE() ORDER BY id"
    
    cur.execute(sql)
    #convert object to data
    result = {
             'dateTime':[],
             'value':[]
             }
    for row in cur:
        result['dateTime'].append(row[0].strftime("%Y-%m-%d %H:%M:%S"))
        result['value'].append(float(row[1]))
        
    conn.commit()
    
    return result

##Get daily data for each sensor
def queryWeeklyData(carId, sensorId):
   #get data from database
   sql = "SELECT time, value FROM carSensor WHERE carId = " + str(carId) + " AND sensorId = " + str(sensorId) + " AND time between date_sub(now(),INTERVAL 1 WEEK) and now() ORDER BY id"
   cur.execute(sql)
   #convert object to data
   result = {
             'dateTime':[],
             'value':[]
            }
   for row in cur:
       time = row[0].strftime("%Y-%m-%d %H:%M:%S")
       result['dateTime'].append(arrow.get(time).replace(tzinfo=dateutil.tz.gettz(tz)))
       result['value'].append(float(row[1]))
       
   conn.commit()
   
   return result

def fillMissingTime(df):
    result_dict = {
            'dateTime':[],
            'value':[]
        }
    for idx in range(df.shape[0]):
        # if idx % 1000 == 0 or idx == df.shape[0]-1:
            # print("-----Filling time index",idx,"of",df.shape[0]-1)
        if idx != 0 and df.index[idx] - df.index[idx-1] > datetime.timedelta(0, 10):
            while df.index[idx] - result_dict['dateTime'][-1] > datetime.timedelta(0, 10):
                result_dict['dateTime'].append(result_dict['dateTime'][-1].replace(seconds=+5))
                result_dict['value'].append(0)
        elif idx != 0 and df.index[idx] - df.index[idx-1] <= datetime.timedelta(0, 10):
            result_dict['dateTime'].append(df.index[idx])
            result_dict['value'].append(df.iloc[idx]['value'])
        else:
            result_dict['dateTime'].append(df.index[0])
            result_dict['value'].append(df.iloc[0]['value'])
    df = pd.DataFrame(data=result_dict).set_index('dateTime')

    # for idx in range(df.shape[0]):
    #     if idx != 0 and df.iloc[idx]['value'] == 0 and idx != df.shape[0]-20:
    #         collect_zero = []
    #         i = idx-1 #start value before 0
    #         j = idx-1
    #         find_non_zero_before_20_zeros = False
    #         while len(collect_zero) != 20 and find_non_zero_before_20_zeros == False:
    #             i += 1
    #             if df.iloc[i]['value'] == 0:
    #                 collect_zero.append(0)
    #             else:
    #                 find_non_zero_before_20_zeros == True
    #         print(idx,find_non_zero_before_20_zeros)
    #         if find_non_zero_before_20_zeros == True:
    #             before_non_zero_value = df.iloc[j]['value']
    #             after_non_zero_value = df.iloc[j+len(collect_zero)]['value']
    #             avg = (before_non_zero_value + after_non_zero_value)/2
    #             for k in range(len(collect_zero)):
    #                 df.set_value(df.index[k+j+1], 'value', avg)
    return df


def cleanNoneValue(df):
    #fill missing value
    for idx in range(df.shape[0]):
        if idx != 0 and idx != (df.shape[0])-1 and df.iloc[idx]['value'] == 'None':
            i = idx
            while df.iloc[i]['value'] == 'None':
                i -= 1
                if df.iloc[i]['value'] != 'None':
                    before_val = df.iloc[i]['value']
            j = idx
            while df.iloc[j]['value'] == 'None':
                j += 1
                if df.iloc[j]['value'] != 'None':
                    after_val = df.iloc[j]['value']
            df.set_value(df.index[idx], 'value', (float(before_val) + float(after_val))/2)
        elif idx == 0 and df.iloc[idx]['value'] == 'None':
            j = idx
            while df.iloc[j]['value'] == 'None':
                j += 1
                if df.iloc[j]['value'] != 'None':
                    after_val = df.iloc[j]['value']
            df.set_value(df.index[idx], 'value', float(after_val))
        elif idx == (df.shape[0])-1 and df.iloc[idx]['value'] == 'None':
            i = idx
            while df.iloc[i]['value'] == 'None':
                i -= 1
                if df.iloc[i]['value'] != 'None':
                    before_val = df.iloc[i]['value']
            df.set_value(df.index[idx], 'value', float(before_val))
    return df

def cleanDataByIQR(df):
    Q1 = np.percentile(df['value'].values, 25)
    Q3 = np.percentile(df['value'].values, 75)
    outlier_threshold = 1.5*(Q3-Q1)
    print("----------", 'value', "-----------")
    print("Q1", Q1)
    print("Q3", Q3)
    print("out", Q1 - outlier_threshold, Q3 + outlier_threshold)
    for idx in range(df['value'].shape[0]):
        print(df.iloc[idx]['value'])
        if df.iloc[idx]['value'] > Q3 + outlier_threshold or df.iloc[idx]['value'] < Q1 - outlier_threshold:
            print("before", df.iloc[idx]['value'])
            df.set_value(df.index[idx], 'value', df['value'].iloc[idx])
            print("after", df.iloc[idx]['value'])   
    return df

def main():
    carId = sys.argv[1]
    range_time = sys.argv[2]
    sensor_name = sys.argv[3]

    if range_time == "day":
        data = queryDailyData(carId, sensor_dict[sensor_name])
        summary = querySummaryDaily(carId)
    elif range_time == "week":
        data = queryWeeklyData(carId, sensor_dict[sensor_name])
        summary = querySummaryWeekly(carId)
    else:
        data = "error wrong time range"
    df_sensor = pd.DataFrame(data=data).set_index('dateTime')
    # print("************************** cleaning None data") 
    df_sensor = cleanNoneValue(df_sensor)
    # print("************************** START Adding missing time") 
    df_sensor = fillMissingTime(df_sensor)
    # print("************************** deleting outlier") 
    # df_sensor = cleanDataByIQR(df_sensor)
    result = {}
    result['data'] = list(df_sensor['value'].values)

    #convert time to label
    label = []
    for idx in df_sensor.index:
        label.append(str(idx.format("YYYY-MM-DD HH:mm:ss")))
    result['label'] = label
    result['summary'] = summary
    cur.close()
    conn.close()
    data_out = json.dumps(result)
    print(data_out)
 # Start process
if __name__ == '__main__':
    main()
