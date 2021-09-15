package com.bridgelabz

import grails.web.servlet.mvc.GrailsParameterMap

class MemberService {

    /**
     * Purpose : To save the data if data is valid and if it has no errors with the help of flush
     * if flush is true then it allows ORM to save the data
     *
     * @param params
     * @return response
     */
    def save(GrailsParameterMap params) {
        Member member = new Member(params)
        def response = AppUtil.saveResponse(false, member)
        if (member.validate()) {
            member.save(flush: true)
            if (!member.hasErrors()) {
                response.isSuccess = true
            }
        }
        return response
    }

    /**
     * Purpose : To update the values of the member with help of properties we can override the old values
     *           with new one and then save the data and return the response
     *
     * @param provides member object and params
     * @param params new values
     * @return response
     */
    def update(Member member, GrailsParameterMap params) {
        // we can override the properties of member with the updated properties parameters
        member.properties = params
        def response = AppUtil.saveResponse(false, member)
        if (member.validate()) {
            member.save(flush: true)
            if (!member.hasErrors()) {
                response.isSuccess = true
            }
        }
        return response
    }

    /**
     * Purpose : To get member data with the help of Id
     *
     * @param id of the member that user wants data
     * @return data of member with given id
     */
    def getById(Serializable id) {
        return Member.get(id)
    }

    /**
     * Purpose : To list all the data of the members in the online contact book
     * and want to show list to the frontend (pagination)
     * @param params
     * @return
     */
    def list(GrailsParameterMap params) {
        params.max = params.max ?: GlobalConfig.itemsPerPage()
        List<Member> memberList = Member.createCriteria().list(params) {
            if (params?.colName && params?.colValue) {
                like(params.colName, "%" + params.colValue + "%")
            }
            if (!params.sort) {
                order("id", "desc")
            }
        } as List<Member>
        return [list: memberList, count: Member.count()]
    }

    /**
     * Purpose : To delete a member from the online contact book
     *
     * @param member
     * @return
     */
    def delete(Member member) {
        try {
            member.delete(flush: true)
        } catch(Exception e) {
            println(e.getMessage())
            return false
        }
        return true
    }

}

