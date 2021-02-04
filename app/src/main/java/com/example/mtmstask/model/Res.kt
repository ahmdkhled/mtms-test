package com.example.mtmstask.model

data class Res<T>(
    var res:T?,
    var loading:Boolean,
    var success:Boolean,
    var error:String?
){

    companion object{
        fun <T> SUCCCESS(res: T): Res<T> {
            return Res(res,false,true,null)
        }
        fun <T> ERROR(res: T?,error:String?): Res<T> {
            return Res(res,false,false,error)
        }
        fun <T> LOADING(): Res<T> {
            return Res(null,true,false,null)
        }


    }
}