package com.a.sample

class TripDsl {

}

data class Trip(var name:String?="",var address:String?="",var departments:List<Department>?=mutableListOf(),var city:List<String>? = mutableListOf(),var culture:String="")


data class Department(var name:String ="",var nameEn:String="")


class TripBuilder {
    var name:String? = ""
    var address:String?=""
    var department = mutableListOf<Department>()
    var city = mutableListOf<String>()

    fun department(block:DepartmentBuilder.()->Unit) {
        var departmentBuilder = DepartmentBuilder()
        block.invoke(departmentBuilder)
        department.add(departmentBuilder.build())
    }

    fun city(block:CityBuilder.()->Unit) {
        var cityBuilder = CityBuilder()
        block.invoke(cityBuilder)
        city.add(cityBuilder.build())
    }

    fun build():Trip = Trip(name,address,department,city)
}

class CityBuilder {
    var city = ""
    fun build():String = city
}

class DepartmentBuilder {
    var name=""
    var nameEn=""

    fun build():Department = Department(name,nameEn)
}

fun trip(block:TripBuilder.()->Unit) :Trip = TripBuilder().apply(block).build()


infix fun Trip.culture(culture: String) {
    this.culture = culture
}

val trip1 = trip{
    name="Trip"
    address = "北京市安定路"
    department{
        name = "机票"
        nameEn = "flight"
    }
    department{
        name="酒店"
        nameEn = "hotel"
    }
    department {
        name="火车票"
        nameEn = "train"
    }
    city{
        city="北京"
    }
    city{
        city="上海"
    }
}


fun main(){
    trip1 culture "Customer Tramwork"
    println(trip1)
}
