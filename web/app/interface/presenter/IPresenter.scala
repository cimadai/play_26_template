package interface.presenter

// ドメイン内で発生したレスポンスモデルを外界に伝えるために変換する責任を持つ
// OUT_REQ, OUT_RESPは、プレゼント先によって変わる
trait IPresenter[OUT_REQ, OUT_RESP] {
  // Interface層でのエラーが発生した時に、その結果をOutsideに対する表現に変換する
  def present(err: interface.InterfaceError)(implicit request: OUT_REQ): OUT_RESP

  // Usecase層での結果をOutsideに対する表現に変換する
  def present(resultModel: domain.responses.IDomainResponse)(implicit request: OUT_REQ): OUT_RESP
}
