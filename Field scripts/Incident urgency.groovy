import com.onresolve.jira.groovy.user.FormField


def product = getFieldByName('Product').value
def urgency = getFieldById('customfield_11500') as FormField
def allServices = getFieldById('customfield_11785')


def blue = ['SC-138' , 'SC-147', 'SC-150','SC-27','SC-156','SC-252','SC-141','SC-144','SC-165', 'SC-168','SC-171','SC-135','SC-132','SC-21','SC-153', 'SC-7677', 'SC-7674', 'SC-7680', 'SC-7683']
def green = ['SC-174', 'SC-177' , 'SC-183']
def purple = ['SC-1178', 'SC-234', 'SC-237', 'SC-240', 'SC-255', 'SC-258', 'SC-264', 'SC-261', 'SC-267','SC-279', 'SC-273', 'SC-276', 'SC-222', 'SC-216','SC-219',
'SC-189','SC-186','SC-7517', 'SC-228', 'SC-210','SC-213','SC-207','SC-231', 'SC-225', 'SC-195','SC-201','SC-198', 'SC-18']
def red = ['SC-249','SC-1208','SC-1073','SC-1052','SC-1043','SC-995','SC-977','SC-1010','SC-1061','SC-504','SC-1076',
'SC-1070','SC-192','SC-1046','SC-989','SC-986','SC-1037','SC-1049','SC-288','SC-291','SC-1034','SC-992','SC-1040','SC-1058',
'SC-285','SC-180','SC-282','SC-980','SC-1004','SC-998','SC-1055','SC-1013','SC-312','SC-1016',
'SC-1019','SC-1022','SC-1025','SC-1064','SC-1031','SC-1028','SC-1007','SC-1001','SC-1067','SC-983','SC-243','SC-246','SC-24',
'SC-306','SC-501','SC-510','SC-270','SC-309','SC-300','SC-315','SC-333','SC-294','SC-324','SC-327','SC-330','SC-303','SC-1205','SC-297',
'SC-336','SC-339','SC-321','SC-318','SC-204', 'SC-7634', 'SC-7598', 'SC-7556','SC-7571','SC-7586','SC-7550','SC-7601','SC-7574']
def type = ''
urgency.setFieldOptions([]) ;


if(!type){
    for (attr in blue) {
        if (attr == product ){
            urgency.setFieldOptions([ 'تاثیر در زمان پیک مصرف', 'تاثیر روی مشتری Gold', 'تاثیر روی مشتری Silver', 'تاثیر روی مشتری عادی','تاثیر روی یک یا تعداد محدود کاربر' ])
            return type = 1 ;
        }
    }
    for (attr in green) {
        if (attr == product ) {
            urgency.setFieldOptions(['کل کشور', 'استان', 'شهرستان/ بانک', 'مشتریان ویژه','پذیرنده' ])
            return type = 2 ;
        }
    }
    for (attr in purple) {
        if (attr == product ) {
            urgency.setFieldOptions([ 'تاثیر در کسب و کار', 'تاثیر در عملکرد شعبه', 'تاثیر در عملکرد دفتر مرکزی', 'تاثیر در عملکرد شرکای تجاری/مرکز تماس','تاثیر روی یک یا تعداد محدود کاربر' ])
            return type = 3 ;
        }
    }
    for (attr in red) {
        if (attr == product ) {
            urgency.setFieldOptions([ 'تاثیر در کسب و کار', 'تاثیر در عملکرد کلیه کارکنان', 'تاثیر در عملکرد یک واحد/ شعبه', 'تاثیر در عملکرد یک اداره','تاثیر روی یک یا تعداد محدود کاربر' ])
            return type = 4 ;
        }
    }
    urgency.setFieldOptions([ 'تاثیر در کسب و کار', 'تاثیر در عملکرد کلیه کارکنان', 'تاثیر در عملکرد یک واحد/ شعبه', 'تاثیر در عملکرد یک اداره','تاثیر روی یک یا تعداد محدود کاربر' ])
    return type = 5 ;
}

