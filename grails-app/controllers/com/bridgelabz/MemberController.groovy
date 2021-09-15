package com.bridgelabz

class MemberController {

    MemberService memberService

    /**
     * Purpose : Default method which gets the list from the service method list of members
     *
     * @return
     */
    def index() {
        def response = memberService.list(params)
        [memberList: response.list, total: response.count]
    }

    /**
     * Purpose : To get details of member by giving its Id
     *
     * @param id taken from user
     * @return data of member with given id
     */
    def details(Integer id) {
        def response = memberService.getById(id)
        if (!response) {
            redirect(controller: "member", action: "index")
        } else {
            [member: response]
        }
    }

    def create() {
        [member: flash.redirectParams]
    }

    /**
     * Purpose : To save the data of members
     *
     * @return
     */
    def save() {
        def response = memberService.save(params)
        if (!response.isSuccess) {
            flash.redirectParams = response.model
            redirect(controller: "member", action: "create")
        }else{
            redirect(controller: "member", action: "index")
        }
    }

    def edit(Integer id) {
        if (flash.redirectParams) {
            [member: flash.redirectParams]
        } else {
            def response = memberService.getById(id)
            if (!response) {
                flash.message = AppUtil.infoMessage(g.message(code: "invalid.entity"), false)
                redirect(controller: "member", action: "index")
            } else {
                [member: response]
            }
        }
    }


    def update() {
        def response = memberService.getById(params.id)
        if (!response) {
            flash.message = AppUtil.infoMessage(g.message(code: "invalid.entity"), false)
            redirect(controller: "member", action: "index")
        } else {
            response = memberService.update(response, params)
            if (!response.isSuccess) {
                flash.redirectParams = response.model
                flash.message = AppUtil.infoMessage(g.message(code: "unable.to.update"), false)
                redirect(controller: "member", action: "edit")
            } else {
                flash.message = AppUtil.infoMessage(g.message(code: "updated"))
                redirect(controller: "member", action: "index")
            }
        }
    }

    def delete(Integer id) {
        def response = memberService.getById(id)
        if (!response) {
            flash.message = AppUtil.infoMessage(g.message(code: "invalid.entity"), false)
            redirect(controller: "member", action: "index")
        } else {
            response = memberService.delete(response)
            if (!response) {
                flash.message = AppUtil.infoMessage(g.message(code: "unable.to.delete"), false)
            } else {
                flash.message = AppUtil.infoMessage(g.message(code: "deleted"))
            }
            redirect(controller: "member", action: "index")
        }
    }

}
